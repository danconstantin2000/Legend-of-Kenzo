package TileMap;
import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
public class TileMap {
    //position
    private Background Bg;
    private double x;
    private double y;
    //bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;
    //map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][]tiles;
    //drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;
    //Constructor
    public TileMap(int tileSize)
    {
        this.tileSize=tileSize;
        /*Cate linii trebuie sa desenam pe ecran
          Ex:tileSize=16,HEIGHT=240-->15 linii
        * */
        numRowsToDraw= GamePanel.HEIGHT/tileSize;
        /*
          Cate coloane trebuie sa desenam pe ecran
          Ex:tileSize=16,WIDTH=320-->20 coloane;
        * */
        numColsToDraw=GamePanel.WIDTH/tileSize+1;//+ Actualizare

    }
    public void loadTiles(String s)
    {
        try
        {
            /*
            Matrice de tiles citita din tilset.png
            BLOCKED-->Tile cu coliziune
            NORMAL-->Tile fara coliziune
            * */
            tileset=ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross=tileset.getWidth()/tileSize;
            tiles=new Tile[3][numTilesAcross];
            BufferedImage subimage;

            for(int col=0;col<numTilesAcross;col++)
            {
                if(col==0)
                {
                    tiles[0][0]=new Tile(null,Tile.NORMAL);
                }
                else
                {
                    subimage=tileset.getSubimage(col*tileSize,0,tileSize,tileSize);
                    tiles[0][col]=new Tile(subimage,Tile.BLOCKED);
                }

                subimage=tileset.getSubimage(col*tileSize,tileSize,tileSize,tileSize);
                tiles[1][col]=new Tile(subimage,Tile.BLOCKED);
                subimage=tileset.getSubimage(col*tileSize,tileSize*2,tileSize,tileSize);
                tiles[2][col]=new Tile(subimage,Tile.NORMAL);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }
    //Citire matrice din fisier
    public void loadMap(String s)
    {
        try{
            InputStream in=getClass().getResourceAsStream(s);
            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            numCols=Integer.parseInt(br.readLine());//Prima linie din fisier(Level1.map)
            numRows=Integer.parseInt(br.readLine());//A doua linie din fisier(Level1.map);
            map=new int[numRows][numCols];//Initializare matrice
            width=numCols*tileSize;//latimea hartii  in pixeli
            height=numRows*tileSize;//inaltimea hartii in pixeli

            xmin=GamePanel.WIDTH-width;
            xmax=0;
            ymin=GamePanel.HEIGHT-height;
            ymax=0;

            String delims="\\s+";//Spatiu alb
            //Citire matrice din fisier
            for(int row=0;row<numRows;row++)
            {
                String line=br.readLine();
                String[] tockens=line.split(delims);
                for(int col=0;col<numCols;col++)
                {
                    map[row][col]=Integer.parseInt(tockens[col]);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    //Setters si getters
    public void setTiles(int i,int j,int nr)
    {
        map[i][j]=nr;
    }
    public int getTileSize(){return tileSize;}
    public double getx(){return x;}
    public double gety(){return y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getNumRows(){return numRows;}
    public int getNumCols(){return numCols;}
    public int getType(int row,int col)
    {
        int rc=map[row][col];
        int r=rc/numTilesAcross;//Linia specifica fiecarui tile din tileset
        int c=rc%numTilesAcross;//Coloana specifica fiecarui tile din tileset
        return tiles[r][c].getType();
    }
    public void setPosition(double x,double y)
    {

        this.x=x;
        this.y=y;
        //fixeaza limitele
        fixBounds();
        //Ce linie/coloana trebuie sa desenam
        colOffset=(int)-this.x/tileSize;
        rowOffset=(int)-this.y/tileSize;


    }
    private void fixBounds()
    {
        if(x<xmin)x=xmin;
        if(y<ymin)y=ymin;
        if(x>xmax)x=xmax;
        if(y>ymax)y=ymax;

    }
    public void draw(Graphics2D g)
    {

        for(int row=rowOffset;row<rowOffset+numRowsToDraw;row++)
        {
            if(row>=numRows)break;//Nu mai avem ce desena
            for(int col=colOffset;col<colOffset+numColsToDraw;col++)
            {
                if(col>=numCols)break;//Nu mai avem ce desena

                //HEREE
                if(map[row][col]==0 ||map[row][col]==11)
                {
                    continue;
                }
                int rc=map[row][col];
                int r=rc/numTilesAcross;
                int c=rc%numTilesAcross;

                g.drawImage(tiles[r][c].getImage(),(int)x+col*tileSize,(int)y+row*tileSize,null);
            }
        }

    }



}
