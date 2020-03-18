#include <stdio.h>

void hexdump(void *data, size_t size)
{
	int i;
	unsigned char *p = data;
	for(i=0; i<size; ++i){
		printf("%02x", p[i]);
	}
	printf("\n");
}
int main(int argc, char **argv)
{
	char buf[40] =  { 0} ;
	filehash(argv[1], buf, sizeof(buf));
	printf("%s\n", buf);
	return 0;
}
