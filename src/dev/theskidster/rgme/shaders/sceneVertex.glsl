#version 330 core

//Non-instanced attributes
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aTexCoords;

//Instanced attributes
layout (location = 4) in vec3 aPosOffset;
layout (location = 5) in vec3 aColOffset;

uniform int uType;
uniform vec3 uColor;
uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec3 ioColor;
out vec2 ioTexCoords;

void main() {
    switch(uType) {
        case 0: //Used for test objects
            ioColor     = aColor;
            gl_Position = uProjection * uView * uModel * vec4(aPosition, 1);
            break;

        case 1: //Used for the scene origin indicator.
            ioColor     = uColor;
            gl_Position = uProjection * uView * uModel * vec4(aPosition, 1);
            break;

        case 2: //Used for drawing the grid of tiles which comprise the scene floor.
            ioColor     = aColOffset;
            ioTexCoords = aTexCoords;
            gl_Position = uProjection * uView * uModel * vec4(aPosition + aPosOffset, 1);
            break;

        case 3: //Used for rendering visible geometry.
            ioColor     = uColor;
            gl_Position = uProjection * uView * uModel * vec4(aPosition, 1);
            break;
    }
}