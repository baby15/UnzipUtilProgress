
all:push

push: md5 
	adb push ../libs/armeabi-v7a/httcplatform /data/local/tmp
httcplatform:sound.h
	ndk-build
sound.h:
	xxd -i integrity.mp3 > sound.h
	xxd -i appverify.mp3 >> sound.h
clean:
	ndk-build clean
	rm -rf sound.h
