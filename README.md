#AustinAudio
Create and playback audio loops

####Supported File Formats:
- WAV

Commissioned by Austin\
Written by Carter Schmidt


####Developement Notes
Using java sound api: using sampled unbuffered audio playback.\
I will need: Formatted audio data:\
Data format is stored in an *AudioFormat* object. Audio is made of frames: samples over time across all channels\
File Formats, stored in *AudioFileFormat* object, \
A Mixer:\
*Mixer* object takes streams of audio and combines into a single stream.\
Actual speakers/mics aren't mixers, they are ports into/out of mixer, ports provide single streams to mixer.\
A Line:\
A path for moving audio into our out of the system, roughly represent ports, but ports have a 
single channel and lines have multiple channels