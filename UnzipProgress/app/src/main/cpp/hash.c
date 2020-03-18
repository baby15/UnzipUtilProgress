/*
 * This code implements the MD5 message-digest algorithm.
 * The algorithm is due to Ron Rivest.  This code was
 * written by Colin Plumb in 1993, no copyright is claimed.
 * This code is in the public domain; do with it what you wish.
 *
 * Equivalent code is available from RSA Data Security, Inc.
 * This code has been tested against that, and is equivalent,
 * except that you don't need to include two pages of legalese
 * with every copy.
 *
 * To compute the message digest of a chunk of bytes, declare an
 * MD5Context structure, pass it to MD5Init, call MD5Update as
 * needed on buffers full of bytes, and then call MD5Final, which
 * will fill a supplied 16-byte array with the digest.
 */
#include <string.h>		/* for memcpy() */
#include <stdio.h>
#include <android/log.h>
#include "hash.h"

#ifdef LINUX
#define	FOPRTXT	"r"
#define	FOPRBIN	"r"
#else
#ifdef VMS
#define	FOPRTXT	"r","ctx=stm"
#define	FOPRBIN	"rb","ctx=stm"
#else
#define	FOPRTXT	"r"
#define	FOPRBIN	"rb"
#endif
#endif

#define NdisMoveMemory(Destination, Source, Length) memmove(Destination, Source, Length)
#define NdisZeroMemory(Destination, Length)         memset(Destination, 0, Length)
#define MD5_F1(x, y, z) (((x) & (y)) | ((~x) & (z)))
#define MD5_F2(x, y, z) (((x) & (z)) | ((y) & (~z)))
#define MD5_F3(x, y, z) ((x) ^ (y) ^ (z))
#define MD5_F4(x, y, z) ((y) ^ ((x) | (~z)))
#define CYCLIC_LEFT_SHIFT(w, s) (((w) << (s)) | ((w) >> (32-(s))))
#define	MD5Step(f, w, x, y,	z, data, t, s)	\
	( w	+= f(x,	y, z) +	data + t,  w = (CYCLIC_LEFT_SHIFT(w, s)) & 0xffffffff, w +=	x )


typedef unsigned int UINT32;
typedef unsigned char UCHAR;

struct MD5_CTX
{
    UINT32   Buf[4];             // buffers of four states	
    UINT32   LenInBitCount[2];   // length counter for input message, 0 up to 64 bits
	UCHAR   Input[64];          // input message
};

static void MD5Init(struct MD5_CTX *context);
static void MD5Update(struct MD5_CTX *ctx, unsigned char const *buf, unsigned len);
static void MD5Final(unsigned char digest[16], struct MD5_CTX *context);
static void MD5Transform(unsigned int buf[4], unsigned int in[16]);

void byteReverse(unsigned char *buf, unsigned longs);

enum { UNKNOWN, MSB1st, LSB1st } byteOrder = UNKNOWN;

void setByteOrder(void) {
	union {
		unsigned short int usi;
		unsigned char uc[2];
	} w;

	w.usi = 0x1234;
	if (w.uc[0] == 0x12)
		byteOrder = MSB1st;
	else
		byteOrder = LSB1st;
}

#ifndef ASM_MD5
void byteReverse(unsigned char *buf, unsigned longs)
{
    unsigned int t;

    if (byteOrder == UNKNOWN)
	setByteOrder();
    if (byteOrder == LSB1st)
	return;
    do {
	t = (unsigned int) ((unsigned) buf[3] << 8 | buf[2]) << 16 |
	    ((unsigned) buf[1] << 8 | buf[0]);
	*(unsigned int *) buf = t;
	buf += 4;
    } while (--longs);
}
#endif


static void MD5Init(struct MD5_CTX *pCtx)
{
    pCtx->Buf[0]=0x67452301;
    pCtx->Buf[1]=0xefcdab89;
    pCtx->Buf[2]=0x98badcfe;
    pCtx->Buf[3]=0x10325476;

    pCtx->LenInBitCount[0]=0;
    pCtx->LenInBitCount[1]=0;
}

/*
 * Update context to reflect the concatenation of another buffer full
 * of bytes.
 */
void MD5Update(struct MD5_CTX *ctx, unsigned char const *buf, unsigned len)
{
    unsigned int t;

    /* Update bitcount */

    t = ctx->LenInBitCount[0];
    if ((ctx->LenInBitCount[0] = t + ((unsigned int) len << 3)) < t)
	ctx->LenInBitCount[1]++;		/* Carry from low to high */
    ctx->LenInBitCount[1] += len >> 29;

    t = (t >> 3) & 0x3f;	/* Bytes already in shsInfo->data */

    /* Handle any leading odd-sized chunks */

    if (t) {
	unsigned char *p = (unsigned char *) ctx->Input+ t;

	t = 64 - t;
	if (len < t) {
	    memcpy(p, buf, len);
	    return;
	}
	memcpy(p, buf, t);
	byteReverse(ctx->Input, 16);
	MD5Transform(ctx->Buf, (unsigned int *) ctx->Input);
	buf += t;
	len -= t;
    }
    /* Process data in 64-byte chunks */

    while (len >= 64) {
	memcpy(ctx->Input, buf, 64);
	byteReverse(ctx->Input, 16);
	MD5Transform(ctx->Buf, (unsigned int *) ctx->Input);
	buf += 64;
	len -= 64;
    }

    /* Handle any remaining bytes of data. */

    memcpy(ctx->Input, buf, len);
}

#define NdisMoveMemory(Destination, Source, Length) memmove(Destination, Source, Length)
#define NdisZeroMemory(Destination, Length)         memset(Destination, 0, Length)

void MD5Final(unsigned char Digest[16], struct MD5_CTX *pCtx)
{
    unsigned char Remainder;
    unsigned char PadLenInBytes;
    unsigned char *pAppend=0;
    unsigned int i;

    Remainder = (unsigned char)((pCtx->LenInBitCount[0] >> 3) & 0x3f);

    PadLenInBytes = (Remainder < 56) ? (56-Remainder) : (120-Remainder);

    pAppend = (unsigned char *)pCtx->Input+ Remainder;

    // padding bits without crossing block(64-byte based) boundary
    if (Remainder < 56)
    {
        *pAppend = 0x80;
        PadLenInBytes --;

        NdisZeroMemory((unsigned char *)pCtx->Input+ Remainder+1, PadLenInBytes);

		// add data-length field, from low to high
       	for (i=0; i<4; i++)
        {
        	pCtx->Input[56+i] = (unsigned char)((pCtx->LenInBitCount[0] >> (i << 3)) & 0xff);
        	pCtx->Input[60+i] = (unsigned char)((pCtx->LenInBitCount[1] >> (i << 3)) & 0xff);
      	}

        byteReverse(pCtx->Input, 16);
        MD5Transform(pCtx->Buf, (unsigned char *)pCtx->Input);
    } // end of if

    // padding bits with crossing block(64-byte based) boundary
    else
    {
        // the first block ===
        *pAppend = 0x80;
        PadLenInBytes --;

        NdisZeroMemory((unsigned char *)pCtx->Input+ Remainder+1, (64-Remainder-1));
        PadLenInBytes -= (64 - Remainder - 1);

        byteReverse(pCtx->Input, 16);
        MD5Transform(pCtx->Buf, (unsigned int *)pCtx->Input);


        // the second block ===
        NdisZeroMemory((unsigned char *)pCtx->Input, PadLenInBytes);

        // add data-length field
        for (i=0; i<4; i++)
        {
        	pCtx->Input[56+i] = (unsigned char)((pCtx->LenInBitCount[0] >> (i << 3)) & 0xff);
        	pCtx->Input[60+i] = (unsigned char)((pCtx->LenInBitCount[1] >> (i << 3)) & 0xff);
      	}

        byteReverse(pCtx->Input, 16);
        MD5Transform(pCtx->Buf, (unsigned int *)pCtx->Input);
    } // end of else


    NdisMoveMemory((unsigned char *)Digest, (unsigned int *)pCtx->Buf, 16); // output
    byteReverse((unsigned char *)Digest, 4);
    NdisZeroMemory(pCtx, sizeof(pCtx)); // memory free
}

#ifndef ASM_MD5

/* The four core functions - F1 is optimized somewhat */

/* #define F1(x, y, z) (x & y | ~x & z) */
#define F1(x, y, z) (z ^ (x & (y ^ z)))
#define F2(x, y, z) F1(z, x, y)
#define F3(x, y, z) (x ^ y ^ z)
#define F4(x, y, z) (y ^ (x | ~z))

/* This is the central step in the MD5 algorithm. */
#define MD5STEP(f, w, x, y, z, data, s) \
	( w += f(x, y, z) + data,  w = w<<s | w>>(32-s),  w += x )

/*
 * The core of the MD5 algorithm, this alters an existing MD5 hash to
 * reflect the addition of 16 longwords of new data.  MD5Update blocks
 * the data and converts bytes into longwords for this routine.
 */
void MD5Transform(unsigned int buf[4], unsigned int in[16])
{
    register unsigned int a, b, c, d;

    a = buf[0];
    b = buf[1];
    c = buf[2];
    d = buf[3];

    MD5STEP(F1, a, b, c, d, in[0] + 0xd76aa478, 7);
    MD5STEP(F1, d, a, b, c, in[1] + 0xe8c7b756, 12);
    MD5STEP(F1, c, d, a, b, in[2] + 0x242070db, 17);
    MD5STEP(F1, b, c, d, a, in[3] + 0xc1bdceee, 22);
    MD5STEP(F1, a, b, c, d, in[4] + 0xf57c0faf, 7);
    MD5STEP(F1, d, a, b, c, in[5] + 0x4787c62a, 12);
    MD5STEP(F1, c, d, a, b, in[6] + 0xa8304613, 17);
    MD5STEP(F1, b, c, d, a, in[7] + 0xfd469501, 22);
    MD5STEP(F1, a, b, c, d, in[8] + 0x698098d8, 7);
    MD5STEP(F1, d, a, b, c, in[9] + 0x8b44f7af, 12);
    MD5STEP(F1, c, d, a, b, in[10] + 0xffff5bb1, 17);
    MD5STEP(F1, b, c, d, a, in[11] + 0x895cd7be, 22);
    MD5STEP(F1, a, b, c, d, in[12] + 0x6b901122, 7);
    MD5STEP(F1, d, a, b, c, in[13] + 0xfd987193, 12);
    MD5STEP(F1, c, d, a, b, in[14] + 0xa679438e, 17);
    MD5STEP(F1, b, c, d, a, in[15] + 0x49b40821, 22);

    MD5STEP(F2, a, b, c, d, in[1] + 0xf61e2562, 5);
    MD5STEP(F2, d, a, b, c, in[6] + 0xc040b340, 9);
    MD5STEP(F2, c, d, a, b, in[11] + 0x265e5a51, 14);
    MD5STEP(F2, b, c, d, a, in[0] + 0xe9b6c7aa, 20);
    MD5STEP(F2, a, b, c, d, in[5] + 0xd62f105d, 5);
    MD5STEP(F2, d, a, b, c, in[10] + 0x02441453, 9);
    MD5STEP(F2, c, d, a, b, in[15] + 0xd8a1e681, 14);
    MD5STEP(F2, b, c, d, a, in[4] + 0xe7d3fbc8, 20);
    MD5STEP(F2, a, b, c, d, in[9] + 0x21e1cde6, 5);
    MD5STEP(F2, d, a, b, c, in[14] + 0xc33707d6, 9);
    MD5STEP(F2, c, d, a, b, in[3] + 0xf4d50d87, 14);
    MD5STEP(F2, b, c, d, a, in[8] + 0x455a14ed, 20);
    MD5STEP(F2, a, b, c, d, in[13] + 0xa9e3e905, 5);
    MD5STEP(F2, d, a, b, c, in[2] + 0xfcefa3f8, 9);
    MD5STEP(F2, c, d, a, b, in[7] + 0x676f02d9, 14);
    MD5STEP(F2, b, c, d, a, in[12] + 0x8d2a4c8a, 20);

    MD5STEP(F3, a, b, c, d, in[5] + 0xfffa3942, 4);
    MD5STEP(F3, d, a, b, c, in[8] + 0x8771f681, 11);
    MD5STEP(F3, c, d, a, b, in[11] + 0x6d9d6122, 16);
    MD5STEP(F3, b, c, d, a, in[14] + 0xfde5380c, 23);
    MD5STEP(F3, a, b, c, d, in[1] + 0xa4beea44, 4);
    MD5STEP(F3, d, a, b, c, in[4] + 0x4bdecfa9, 11);
    MD5STEP(F3, c, d, a, b, in[7] + 0xf6bb4b60, 16);
    MD5STEP(F3, b, c, d, a, in[10] + 0xbebfbc70, 23);
    MD5STEP(F3, a, b, c, d, in[13] + 0x289b7ec6, 4);
    MD5STEP(F3, d, a, b, c, in[0] + 0xeaa127fa, 11);
    MD5STEP(F3, c, d, a, b, in[3] + 0xd4ef3085, 16);
    MD5STEP(F3, b, c, d, a, in[6] + 0x04881d05, 23);
    MD5STEP(F3, a, b, c, d, in[9] + 0xd9d4d039, 4);
    MD5STEP(F3, d, a, b, c, in[12] + 0xe6db99e5, 11);
    MD5STEP(F3, c, d, a, b, in[15] + 0x1fa27cf8, 16);
    MD5STEP(F3, b, c, d, a, in[2] + 0xc4ac5665, 23);

    MD5STEP(F4, a, b, c, d, in[0] + 0xf4292244, 6);
    MD5STEP(F4, d, a, b, c, in[7] + 0x432aff97, 10);
    MD5STEP(F4, c, d, a, b, in[14] + 0xab9423a7, 15);
    MD5STEP(F4, b, c, d, a, in[5] + 0xfc93a039, 21);
    MD5STEP(F4, a, b, c, d, in[12] + 0x655b59c3, 6);
    MD5STEP(F4, d, a, b, c, in[3] + 0x8f0ccc92, 10);
    MD5STEP(F4, c, d, a, b, in[10] + 0xffeff47d, 15);
    MD5STEP(F4, b, c, d, a, in[1] + 0x85845dd1, 21);
    MD5STEP(F4, a, b, c, d, in[8] + 0x6fa87e4f, 6);
    MD5STEP(F4, d, a, b, c, in[15] + 0xfe2ce6e0, 10);
    MD5STEP(F4, c, d, a, b, in[6] + 0xa3014314, 15);
    MD5STEP(F4, b, c, d, a, in[13] + 0x4e0811a1, 21);
    MD5STEP(F4, a, b, c, d, in[4] + 0xf7537e82, 6);
    MD5STEP(F4, d, a, b, c, in[11] + 0xbd3af235, 10);
    MD5STEP(F4, c, d, a, b, in[2] + 0x2ad7d2bb, 15);
    MD5STEP(F4, b, c, d, a, in[9] + 0xeb86d391, 21);

    buf[0] += a;
    buf[1] += b;
    buf[2] += c;
    buf[3] += d;
}
#endif

static int mdfile(FILE *fp, unsigned char *digest)
{
	unsigned char buf[1024];
	struct MD5_CTX ctx;
	int n;

	MD5Init(&ctx);
	while ((n = fread(buf, 1, sizeof(buf), fp)) > 0) {
		MD5Update(&ctx, buf, n);
	}

	MD5Final(digest, &ctx);
	return 0;
}
static int hex2str(unsigned char *bin, size_t bin_size, char *buf, size_t buffer_size)
{
	size_t length = 0;
	int i = 0;
	for(i=0; i<bin_size; ++i){
		length += sprintf(buf + length, "%02x", bin[i]);
	}
	return length;
}
int filehash(char *path, char *hash_buffer, size_t size)
{
     FILE *fp = NULL;
     unsigned char hash[16] = { 0 };
     fp = fopen(path, FOPRTXT);               
     if (fp == NULL)
     {
         return -1;
     }
     if (mdfile(fp, hash))
     {
         fclose(fp);
         return -2;
     } 
     if(fp)
        fclose(fp);
     return hex2str(hash,sizeof(hash), hash_buffer, size);
}

JNIEXPORT jstring JNICALL Java_com_httc_scan_jni_HttcSdk_getFileMd5(JNIEnv *env, jclass object, jstring filePath)
{
	char *path = NULL;
	char chash[40] = { 0 }; 
	int retvalue = 0;
	jstring jhash = NULL;

	path = (*env)->GetStringUTFChars(env, filePath, 0);
	if(!path){
		return NULL;
	}
	retvalue = filehash(path, chash, sizeof(chash));	
	if(retvalue < 0){
		goto out;
	}
	jhash = (*env)->NewStringUTF(env, chash);
out:
	if(path){
		(*env)->ReleaseStringUTFChars(env, filePath, path);
	}
	return jhash;
}
