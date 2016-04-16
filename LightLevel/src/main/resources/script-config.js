// Determines the color of the light particles
// Should return the color's RGB value
function particleColor(lightLevel) {
     if(lightLevel > 12) {
        return 65280;// LIME
     } else if (lightLevel >= 12) {
        return 32768;// GREEN
     } else if (lightLevel >= 8) {
        return 16776960;// YELLOW
     } else {
        return 16711680;// RED
     }
}