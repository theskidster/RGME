#version 330 core

in vec3 ioColor;
in vec2 ioTexCoords;

uniform int uType;
uniform sampler2D uTexture;

out vec4 ioResult;

void makeTransparent(float a) {
    if(a == 0) discard;
}

void main() {
    switch(uType) {
        case 0: case 1: case 2:
            ioResult = vec4(ioColor, 0);
            break;
    }
}