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
    /*
    GEOMETRIC TYPES:
        0 - test objects
        1 - scene origin indicator (Origin.java)
        2 - grid tiles that comprise scene floor (Floor.java)
        3 - visible level geometry (VisibleGeometry.java)
        4 - light source icons ()
    */
    
    switch(uType) {
        case 0: case 1: case 3:
            ioResult = vec4(ioColor, 0);
            break;

        case 2: case 4:
            makeTransparent(texture(uTexture, ioTexCoords).a);
            ioResult = texture(uTexture, ioTexCoords) * vec4(ioColor, 1);
            break;
    }
}