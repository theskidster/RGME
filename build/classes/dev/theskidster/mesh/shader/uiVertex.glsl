#version 330 core

layout (location = 0) in vec2 aPosition;
layout (location = 2) in vec3 aColor;

uniform mat4 uProjection;

out vec3 ioColor;

void main() {
    ioColor     = aColor;
    gl_Position = uProjection * vec4(aPosition, 0, 1);
}