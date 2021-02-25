#version 330 core

in vec2 ioTexCoords;
in vec3 ioColor;

uniform int uType;
uniform vec3 uFontColor;
uniform sampler2D uTexture;

out vec4 ioResult;

void main() {
    switch(uType) {
        case 0: //Used for font rendering.
            vec4 sampled = vec4(1, 1, 1, texture(uTexture, ioTexCoords).r);
            ioResult     = vec4(uFontColor, 1) * sampled;
            break;
    }
}