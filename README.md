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

> [!IMPORTANT]
> Entity class will load the texture for you. You only have to pass file name

```declarative
OBJLoader objLoader = new OBJLoader();
Mesh car;

carMesh = objLoader.loadObj("mymodel");

// Entity(GL2, Mesh, String, Vec3f) gl, mesh, texture file name, position
Entity car = new Entity(gl, carMesh, "mytexture.png", new Vec3f(0.0f, 0.0f, 0.0f));

// In display function can be used as
car.render();

// more can refer to example in code
```

> [!WARNING]
> There are probably some error handling that needs to be done.
> Use function with caution. Any question can ask me in group chat. Any bug can roger in group chat