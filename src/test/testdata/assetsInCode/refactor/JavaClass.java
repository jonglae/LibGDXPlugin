import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class SomeClass {

  @GDXAssets(skinFiles = "src\\refactor/libgdx.skin")
  static Skin staticSkin;

  @GDXAssets(skinFiles = {"src/findUsages/findUsages1.skin", "src\\refactor\\libgdx.skin"})
  private Skin skin;

  void f() {
    staticSkin.get("green", TextButton.TextButtonStyle.class);
    skin.remove("green", TextButton.TextButtonStyle.class)
  }

}