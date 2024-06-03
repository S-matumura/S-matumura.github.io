import javax.swing.SwingUtilities;

//メインメソッド
public class Main extends Object {
    public static void main(String[]args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ThirtySecondGame();
            }
        });
    }
    
}