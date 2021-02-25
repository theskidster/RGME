#version 330 core

layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec2 aTexCoords;
layout (location = 2) in vec3 aColor;

uniform int  uType;
uniform mat4 uProjection;

out vec2 ioTexCoords;
out vec3 ioColor;

void main() {
    switch(uType) {
        case 0: //Used for font rendering.
            ioTexCoords = aTexCoords;
            ioColor     = aColor;
            gl_Position = uProjection * vec4(aPosition, 0, 1);
            break;
    }
}