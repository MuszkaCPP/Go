package go.mik.Client;

import go.mik.UI.UIWindow;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player implements Client {
    private String nickName;
    private Scanner input;
    private PrintWriter output;
    private UIWindow _gameWindow;

    public Player(String nickName, String serverAddress, int socketPort) throws Exception {
        this.nickName = nickName;
        Socket socket = new Socket(serverAddress, socketPort);
        this.input = new Scanner(socket.getInputStream());
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this._gameWindow = new UIWindow(this, nickName);

        //new TestSender(this.nickName, this); // TEST OF SENDING CLIENT -> SERVER -> CLIENT
    }

    @Override
    public void start() {
        System.out.println("Player is Running");
        try {
            output.println(nickName);

            String response;
            while (input.hasNextLine()) {
                response = input.nextLine();
                System.out.println(response);

               if (response.startsWith("CHAT:")) {
                    response = response.replaceFirst("CHAT:", "");
                    this.sendToChat(response);
                } else if (response.startsWith("GAME:")) {
                   response = response.replaceFirst("GAME:", "");
                   this.setStones(response);
                    // UI.setStones <= response // thread na ui ktory przekazuje response albo parsuje i przekazuje response // może tak zrobimy
                }
            }
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public String getPlayerName(){
        return this.nickName;
    }

    @Override
    public void sendToChat(String message) {
        _gameWindow.getMessageForChat(message + "\n");
    }

    @Override
    public void sendToOpponentChat(String message) {
        this.output.println(message);
    }

    @Override
    //wysylanie z ui do servera info o probie polozenia kloca || Splitter ---> ";"
    public void move(String position) {
        this.output.println("MOVE:" + position);
        /*setStones("bwwobbbbbbbbbbbbbbb" +
                "ooooooooooooooooooo" +  "ooooooooooooooooooo" +"bboooooooooooooooob" +  "booooooooooooooooob"
                +  "ooooooooooooooooooo"+  "ooooooooooooooooooo" +  "booooooooooooooooob"
                +  "ooooooooooooooooooo" +  "ooooooooooooooooooo" +  "ooooooooooooooooooo"
                +  "ooooooooooooooooooo" +  "ooooooooooooooooooo" +  "ooooooooooooooooooo"
                +  "ooooooooooooooooooo" +  "ooooooooooooooooooo" +  "ooooooooooooooooooo"
                + "ooooooooooooooooooo" +  "ooooooooooooooooooo" +  "oooooooooooooooooob");*/
    }

    @Override
    //odbieranie z serwera mapy z polozeniem klockow
    public void setStones(String gameSet) {
        if(gameSet.startsWith("put:")){
            gameSet = gameSet.replaceFirst("put:", "");
            try{
                String[] str = gameSet.split(";");

                if(str[2].equals("b")){
                    this._gameWindow.getUI_Field().addStoneToList("black", Integer.parseInt(str[0]), Integer.parseInt(str[1]));
                }else if(str[2].equals("w")){
                    this._gameWindow.getUI_Field().addStoneToList("white", Integer.parseInt(str[0]), Integer.parseInt(str[1]));
                }
            }catch(NumberFormatException ex){
                System.err.println(ex.getMessage());
            }
        }
        else {
            for (int i = 1; i <= _gameWindow.getUI_Field().getVerFieldAmount(); i++) {
                for (int j = 0; j <= _gameWindow.getUI_Field().getVerFieldAmount(); j++) {

                    if (gameSet.charAt(j + (_gameWindow.getUI_Field().getVerFieldAmount() + 1) * (i - 1)) == 'b') {

                        this._gameWindow.getUI_Field().addStoneToList("black", j + 1, i);

                    } else if (gameSet.charAt(j + (_gameWindow.getUI_Field().getVerFieldAmount() + 1) * (i - 1)) == 'w') {

                        this._gameWindow.getUI_Field().addStoneToList("white", j + 1, i);
                    }
                }
            }
        }
    }

    @Override
    public void quit() {

    }
}
