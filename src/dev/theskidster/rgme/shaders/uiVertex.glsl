#version 330 core

layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec2 aTexCoords;

uniform int  uType;
uniform vec2 uTexCoords;
uniform vec2 uPosition;
uniform mat4 uProjection;

out vec2 ioTexCoords;

void main() {
    switch(uType) {
        case 0: case 1: //Used for font rendering and backgrounds
            ioTexCoords = aTexCoords;
            gl_Position = uProjection * vec4(aPosition, 0, 1);
            break;

        case 2: //Used for icons.
            ioTexCoords = aTexCoords + uTexCoords;
            gl_Position = uProjection * vec4(aPosition + uPosition, 0, 1);
            break;
    }
}