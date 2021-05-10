package GameState;


import java.awt.*;
public class GameStateManager {
    /*
        -GameState[] gameStates;
        -int currentState;
        +static final int NUMGAMESTATES;
        +static final int MENUSTATE;
        +static final int LEVEL1STATE;
        +static final int GAMEOVERSTATE;
        +static final int LOADING STATE;
        Metode:
        +GameStateManager()
        -void loadState(int state)
        -void unloadState(int state)
        +void setState(int state)
        +void update()
        +void draw(Graphics2D g)
        +void keyPressed(int k)
        +void keyReleased(int k)
        Clasa cu rolul de manager al State-urilor

    * */
    private GameState[] gameStates;//GameStates array;
    private int currentState;//State curent
    public static final int NUMGAMESTATES=8;//Numar total de State-uri
    public static final int MENUSTATE=0;
    public static final int LEVEL1STATE=1;
    public static final int GAMEOVERSTATE=2;
    public static final int LOADINGSTATE=3;
    public static final int LOADINGSTATE2=4;
    public static final int LEVEL2STATE=5;
    public static final int GAMEOVERSTATE2=6;
    public static final int HELPSTATE=7;
    //La alegerea optiunii Start din meniu dureaza putin sa se incarce toate obiectele
    //pe harta asa ca am facut acest state "intermediar"de unde se face intrarea in level1State.

    //Constructor GameStateManager
    public GameStateManager()
    {
        gameStates=new GameState[NUMGAMESTATES];
        currentState=MENUSTATE;//Seteaza State-ul curent ca fiind MENUSTATE.
        loadState(currentState);//Incarca state-ul curent.
    }


    private void loadState(int state)
    {
        /*
            if(state==flag)
            {
                    Instantiere state.
            }
         */
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
    }

    private void unloadState(int state)
    {
        gameStates[state]=null;
    }


    public void setState(int state)
    {
        //Seteaza un nou state, punand state-ul current pe null
        unloadState(currentState);
        currentState=state;
        loadState(currentState);

    }

    //Update currentState
    public void update() {
            if(gameStates[currentState] != null) {

                gameStates[currentState].update();
            }
    }
    //draw CurrentState
    public void draw(Graphics2D g)
    {
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
