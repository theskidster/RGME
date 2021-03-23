#version 330 core

#define MAX_LIGHTS 32

in vec3 ioColor;
in vec2 ioTexCoords;
in vec3 ioNormal;
in vec3 ioFragPos;

struct Light {
    float brightness;
    float contrast;
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
};

uniform int uType;
uniform int uNumLights;
uniform sampler2D uTexture;
uniform Light uLights[MAX_LIGHTS];

out vec4 ioResult;

void makeTransparent(float a) {
    if(a == 0) discard;
}

vec3 calcWorldLight(Light light, vec3 normal) {
    vec3 direction = normalize(light.position);
    float diff     = max(dot(normal, direction), -light.contrast);
    vec3 diffuse   = diff * light.ambient * light.diffuse;

    return (light.ambient + diffuse) * light.brightness;
}

vec3 calcPointLight(Light light, vec3 normal, vec3 fragPos) {
    vec3 ambient = light.ambient;

    vec3 direction = normalize(light.position - ioFragPos);
    float diff     = max(dot(normal, direction), -light.contrast);
    vec3 diffuse   = diff * light.diffuse;

    float linear    = 0.0014f / light.brightness;
    float quadratic = 0.000007f / light.brightness;
    float dist      = length(light.position - ioFragPos);
    float attenuate = 1.0f / (1.0f + linear * dist + quadratic * (dist * dist));

    ambient *= attenuate;
    diffuse *= attenuate;

    return (ambient + diffuse) * light.brightness;
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
        case 0: case 1:
            ioResult = vec4(ioColor, 0);
            break;

        case 2: case 4:
            makeTransparent(texture(uTexture, ioTexCoords).a);
            ioResult = texture(uTexture, ioTexCoords) * vec4(ioColor, 1);
            break;

        case 3:
            vec3 normal = normalize(ioNormal);
            vec3 result = calcWorldLight(uLights[0], normal);

            for(int i = 1; i < uNumLights; i++) {
                result += calcPointLight(uLights[i], normal, ioFragPos);
            }
            
            makeTransparent(texture(uTexture, ioTexCoords).a);
            ioResult = texture(uTexture, ioTexCoords) * vec4(result, 1);
            break;
    }
}