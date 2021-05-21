package GameState;


import java.awt.*;
public class GameStateManager {
    private GameState[] gameStates;
    private int currentState;
    public static final int NUMGAMESTATES=10;
    public static final int MENUSTATE=0;
    public static final int LEVEL1STATE=1;
    public static final int GAMEOVERSTATE=2;
    public static final int LOADINGSTATE=3;
    public static final int LOADINGSTATE2=4;
    public static final int LEVEL2STATE=5;
    public static final int GAMEOVERSTATE2=6;
    public static final int HELPSTATE=7;
    public static final int ENDSTATE=8;
    public static final int SETTINGSSTATE=9;

    public GameStateManager() {
        gameStates=new GameState[NUMGAMESTATES];
        currentState=MENUSTATE;
        loadState(currentState);
    }
    private void loadState(int state) {
        if(state==MENUSTATE)
        {
            gameStates[state]=new MenuState(this);
        }
        else if(state==LEVEL1STATE)
        {
            gameStates[state]=new Level1State(this);
        }
        else if(state==GAMEOVERSTATE)
        {
            gameStates[state]=new GameOverState(this);
        }
        else if(state==LOADINGSTATE)
        {
            gameStates[state]=new LoadingState(this);
        }
        else if(state==LOADINGSTATE2)
        {
            gameStates[state]=new LoadingState2(this);
        }
        else if(state==LEVEL2STATE)
        {
            gameStates[state]=new Level2State(this);
        }
        else if (state==GAMEOVERSTATE2)
        {
            gameStates[state]=new GameOverState2(this);
        }
        else if(state==HELPSTATE)
        {
            gameStates[state]=new HelpState(this);
        }
        else if(state==ENDSTATE)
        {
            gameStates[state]=new EndState(this);
        }
        else if(state==SETTINGSSTATE)
        {
            gameStates[state]=new SettingsState(this);
        }
    }
    private void unloadState(int state)
    {
        gameStates[state]=null;
    }
    public void setState(int state) {
        unloadState(currentState);
        currentState=state;
        loadState(currentState);
    }
    public void update() {
            if(gameStates[currentState] != null) {

                gameStates[currentState].update();
            }
    }
    public void draw(Graphics2D g) {
        try {
            if(gameStates[currentState] != null) gameStates[currentState].draw(g);
        }catch(Exception e){}
    }
    public void keyPressed(int k)
    {
       if(gameStates[currentState]!=null) gameStates[currentState].keyPressed(k);
    }
    public void keyReleased(int k)
    {
        if(gameStates[currentState]!=null) gameStates[currentState].keyReleased(k);
    }

}
