# Not Earth

A landscape project. But it's not earth.

## Utility classes

### Loading a texture
To load a texture, simply use TextureLoader.loadTexture static method.
This class only support 1 texture (for now).
```java
import com.notearth.texture.TextureLoader;

Texture myTexture;

myTexture = TextureLoader.loadTexture(gl, "myTexture.jpg");
```

### Import model from blender
Model from blender need to be exported with `triangulate mesh` option ticked.
Model should be exported as wavefront obj. OBJ file should be placed
in `resources/models` folder. Texture should be in `resources/textures` folder.
Use OBJLoader class to load an OBJ file.

```declarative
OBJLoader objLoader = new OBJLoader();
RawMeshBuilder car;

car = objLoader.loadObj("mymodel");

// In display function can be used as
// Apply translation (gl.glTranslate) or rotation (gl.glRotate) here 
car.render(gl, myTexture);
```

> [!WARNING]
> There are probably some error handling that needs to be done.
> Use function with caution. Any question can ask me in group chat. Any bug can roger in group chat