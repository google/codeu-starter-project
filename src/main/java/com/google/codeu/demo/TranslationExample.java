import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class TranslationExample {
  public static void main(String[] args) throws Exception {

    Translate translate = TranslateOptions.getDefaultInstance().getService();

    String originalText = "Hello world";

    Translation translation =
        translate.translate(originalText, TranslateOption.targetLanguage("es"));
    String translatedText = translation.getTranslatedText();

    System.out.println(translatedText);
  }
}