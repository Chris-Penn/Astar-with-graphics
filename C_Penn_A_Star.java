
package c_penn_a_star;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import javax.swing.*;


/**
 *
 * @author chris penn
 */
public class C_Penn_A_Star {

   private final static int ADJACENT_COST = 10;
   private final static int DIAGONAL_COST = 14;
   private final static int ROWS = 15;
   private final static int COLUMNS = 15;  
   
   private static Node[][] nodeMap = new Node[COLUMNS][ROWS];
   private static PriorityQueue <Node> openList = new PriorityQueue <Node>();
   private static LinkedList <Node> closedList = new LinkedList <Node>();
   private static Node startNode;
   private static Node goalNode; 
   private static boolean selectedStart = false;
   private static boolean selectedGoal = false;
   private static JPanel MyPanel = new JPanel();
   private static JButton[][] buttonMap = new JButton[COLUMNS][ROWS];
   private static int runningG = 0;

   
    public static void main(String[] args) {
         
        //create GUI
        JFrame f = new JFrame("A* Search");   
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
   	MyPanel.setLayout( new GridLayout(ROWS, COLUMNS) ); 	
        
        //Align nodes with GUI buttons
   	for(int i = 0; i<COLUMNS; i++){      
            for(int j = 0; j<ROWS; j++){
                int x = j;
                int y = i;
                buttonMap [x][y] = new JButton();
                nodeMap [x][y] = new Node(x, y, 0);
                buttonMap[x][y].setBackground(Color.GREEN);
    
                //set start nodes 
                buttonMap[x][y].addActionListener((ActionEvent e) -> {
                if(!selectedStart){
                    buttonMap[x][y].setBackground(Color.YELLOW);
                    startNode = new Node (x, y, 0);
                    startNode.setParent(null);                  
                    startNode.setG(0);
                    closedList.add(startNode);
                    System.out.println("Start node: " + startNode.getRow() + "," + startNode.getCol());
                    buttonMap[x][y].setEnabled(false);
                    selectedStart = true;
                }
                //select goal node
                else if(!selectedGoal){
                    buttonMap[x][y].setBackground(Color.RED);
                    goalNode = new Node (x, y, 0);
                    System.out.println("Goal node: " + goalNode.getRow() + "," + goalNode.getCol());
                    buttonMap[x][y].setEnabled(false);
                    selectedGoal = true;
                    findNextStep(closedList.poll());

                }
                //disable controls
                else{
                    
                }
                }); 
                MyPanel.add(buttonMap[x][y]);   
            }
        }
        
        //Generate unpathable nodes
       Random rnd = new Random(); 
        int numUnpathable = 0;
        while(numUnpathable < 23){
            int rndX = rnd.nextInt(COLUMNS);
            int rndY = rnd.nextInt(ROWS);
            if(buttonMap[rndX][rndY].getBackground().equals(Color.GREEN)){
                buttonMap[rndX][rndY].setBackground(Color.BLUE);
                buttonMap[rndX][rndY].setEnabled(false);
                numUnpathable++;  
            }   
        }
        
   	f.getContentPane().add(MyPanel, "Center");       
   	f.setSize(900,900); 
   	f.setVisible(true);
        
    }
    
    public static void findNextStep(Node n){
        
        //find best H and best F to move to next node
        int minH = calculateH(n);
        int minF = n.getF();
        Node tempN = new Node (n.getCol(), n.getRow(), 1);
        for (int i = 0; i < COLUMNS; i++){
            for(int j = 0; j < ROWS; j++){
                if (Math.abs(n.getRow() - j) <= 1 && Math.abs(n.getCol() - i) <= 1 && buttonMap[j][i].isEnabled()){
                    calculateH(nodeMap[j][i]);             
                    if(calculateH(nodeMap[j][i]) < minH ){ 
                       
                        tempN = nodeMap[j][i];
                        
                        if(tempN.getCol() != n.getCol() && tempN.getRow() != n.getRow()){
                            tempN.setG(runningG + DIAGONAL_COST);
                        }
                        else{
                            tempN.setG(runningG + ADJACENT_COST);
                        }
                        tempN.setF();
                        if(nodeMap[j][i].getF() < minF){                      
                            tempN = nodeMap[j][i];
                            minF = tempN.getF();
                        }
                    }  
                }
            }
        }
        //increment G cost
        if(tempN.getCol() != n.getCol() && tempN.getRow() != n.getRow()){
            tempN.setG(runningG += DIAGONAL_COST);
        }
        else{
            tempN.setG(runningG += ADJACENT_COST);
        }
        //set parent to previous node
        tempN.setParent(n);
        n = tempN;
        closedList.add(n);
        buttonMap[n.getRow()][n.getCol()].setBackground(Color.ORANGE);
        System.out.println(closedList.peek().getRow()+ ", " + closedList.peek().getCol() + " G: " + closedList.peek().getG());
        n.setF();
        //check for last node
        if(calculateH(closedList.peek()) >= 19){     
            findNextStep(closedList.poll());
        }
        //exit recursion
        else{
            if(n.getCol() != goalNode.getCol() && n.getRow() != goalNode.getRow()){
                tempN.setG(runningG += DIAGONAL_COST);
            }
            else{
                tempN.setG(runningG += ADJACENT_COST);
            }
            System.out.println(goalNode.getRow()+ ", " + goalNode.getCol() + " G: " + runningG + " GOAL!");
        }
    }
    
    public static int calculateH(Node n){
        
        int distX = Math.abs(goalNode.getCol() - n.getCol());
        int distY = Math.abs(goalNode.getRow() - n.getRow());
        int h = (10* (distX + distY));
        return h;
    }
  
   
}
