#include <string.h>
#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <errno.h>
#include <android/log.h>
#include "hash.h"

#include "openssl/sm3.h"
#include "openssl/md5.h"
#include "openssl/sha.h"


#define TAG "HTTC"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) 
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG ,__VA_ARGS__) 
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)


static int hex2str(unsigned char *bin, size_t bin_size, char *buf, size_t buffer_size)
{
	size_t length = 0;
	int i = 0;
	for(i=0; i<bin_size; ++i){
		length += sprintf(buf + length, "%02x", bin[i]);
	}
	return length;
}
int filehash_sha256(const char *path, char *buf, size_t size)
{
	int fd = -1;
	void *m = NULL;
	struct stat st;
	int ret = -1;
	unsigned char dgst[SHA256_DIGEST_LENGTH] = { 0 };
	SHA256_CTX ctx;

	fd = open(path, O_RDONLY);
	if(fd < 0){
		LOGE("打开文件 %s : %s .\n", path, strerror(errno));
		goto out;
	}
	ret = fstat(fd, &st);
	if(ret){
		LOGE("文件 STAT  %s : %s .\n", path, strerror(errno));
		goto out;
	}
	m = mmap(NULL, st.st_size, PROT_READ, MAP_SHARED, fd, 0);
	if(m == MAP_FAILED){
		m = NULL;
		LOGE("MMAP %s : %s .\n", path, strerror(errno));
		goto out;
	}
	SHA256_Init(&ctx);
	SHA256_Update(&ctx, m, st.st_size);
	SHA256_Final(dgst, &ctx);
	ret = hex2str(dgst, sizeof(dgst), buf, size);
out:
	if(m){
		munmap(m, st.st_size);
	}
	if(fd > 0){
		close(fd);
	}
	return ret;
}
int filehash_md5(const char *path, char *buf, size_t size)
{
	int fd = -1;
	void *m = NULL;
	struct stat st;
	int ret = -1;
	unsigned char dgst[MD5_DIGEST_LENGTH] = { 0 };
	MD5_CTX ctx;

	fd = open(path, O_RDONLY);
	if(fd < 0){
		LOGE("打开文件 %s : %s .\n", path, strerror(errno));
		goto out;
	}
	ret = fstat(fd, &st);
	if(ret){
		LOGE("fstat %s : %s .\n", path, strerror(errno));
		goto out;
	}
	m = mmap(NULL, st.st_size, PROT_READ, MAP_SHARED, fd, 0);
	if(m == MAP_FAILED){
		m = NULL;
		LOGE("mmap %s : %s .\n", path, strerror(errno));
		goto out;
	}
	MD5_Init(&ctx);
	MD5_Update(&ctx, m, st.st_size);
	MD5_Final(dgst, &ctx);
	ret = hex2str(dgst, sizeof(dgst), buf, size);
out:
	if(m){
		munmap(m, st.st_size);
	}
	if(fd > 0){
		close(fd);
	}
	return ret;
}

int filehash_sm3(const char *path, char *buf, size_t size)
{
	int fd = -1;
	void *m = NULL;
	struct stat st;
	int ret = -1;
	unsigned char dgst[SM3_DIGEST_LENGTH] = { 0 };
	SM3_CTX ctx;

	fd = open(path, O_RDONLY);
	if(fd < 0){
		LOGE("open %s : %s .\n", path, strerror(errno));
		goto out;
	}
	ret = fstat(fd, &st);
	if(ret){
		LOGE("fstat %s : %s .\n", path, strerror(errno));
		goto out;
	}
	m = mmap(NULL, st.st_size, PROT_READ, MAP_SHARED, fd, 0);
	if(m == MAP_FAILED){
		m = NULL;
		LOGE("mmap %s : %s .\n", path, strerror(errno));
		goto out;
	}
	sm3_init(&ctx);
	sm3_update(&ctx, m, st.st_size);
	sm3_final(dgst, &ctx);
	ret = hex2str(dgst, sizeof(dgst), buf, size);
out:
	if(m){
		munmap(m, st.st_size);
	}
	if(fd > 0){
		close(fd);
	}
	return ret;
}
JNIEXPORT jstring JNICALL Java_com_httc_scan_jni_HttcSdk_getFileSm3(JNIEnv *env, jclass object, jstring filePath)
{
	const char *path = NULL;
	char chash[SM3_DIGEST_LENGTH*2 + 1] = { 0 }; 
	int retvalue = 0;
	jstring jhash = NULL;

	path = (*env)->GetStringUTFChars(env, filePath, 0);
	if(!path){
		return NULL;
	}
	retvalue = filehash_sm3(path, chash, sizeof(chash));	
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
JNIEXPORT jstring JNICALL Java_com_httc_scan_jni_HttcSdk_getFileSha256(JNIEnv *env, jclass object, jstring filePath)
{
	const char *path = NULL;
	char chash[SHA256_DIGEST_LENGTH *2 + 1] = { 0 }; 
	int retvalue = 0;
	jstring jhash = NULL;

	path = (*env)->GetStringUTFChars(env, filePath, 0);
	if(!path){
		return NULL;
	}
	LOGI("计算文件 %s 摘要", path);
	retvalue = filehash_sha256(path, chash, sizeof(chash));	
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
JNIEXPORT jstring JNICALL Java_com_httc_scan_jni_HttcSdk_getFileMd5(JNIEnv *env, jclass object, jstring filePath)
{
	const char *path = NULL;
	char chash[MD5_DIGEST_LENGTH*2 + 1] = { 0 }; 
	int retvalue = 0;
	jstring jhash = NULL;

	path = (*env)->GetStringUTFChars(env, filePath, 0);
	if(!path){
		return NULL;
	}
	retvalue = filehash_md5(path, chash, sizeof(chash));	
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
