#include <stdio.h>
#include <string.h>

int main(void) {
    char buff[15];
    int pass = 0;

    printf("Enter the password: ");
    gets(buff);

    if(strcmp(buff, "gururaja")) {
        printf("Wrong Password\n");
    } else {
        printf("Correct Password\n");
        pass = 1;
    }

    if (pass) {
        printf("Root privileges given to the user\n");
    }
}