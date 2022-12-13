TARGET = cachesim
SRC    = $(TARGET).c
CC     = gcc
CFLAGS = -g -std=c99 -Wall -Wvla  -fsanitize=address,undefined

$(TARGET): $(SRC)
	$(CC) $(CFLAGS) -o $@ $^ -lm

clean:
	rm -rf $(TARGET) *.o *.a *.dylib *.dSYM
