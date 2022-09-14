import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
public class A1083337_checkpoint7_RouteFinder {
    //Description : The target block.
    private A1083337_checkpoint7_Block target;
    //Description : The hashmap that records the parent block.
    private HashMap<A1083337_checkpoint7_Block, A1083337_checkpoint7_Block> ParentBlock;
    //Description : Record which block has been visited.
    private boolean[][] visited ;
    // Description : The root frame.
    private A1083337_checkpoint7_GameFrame parentFrame;
    //Description : the map with all blocks.
    //You can get the location block you want with typing map[x][y].
    private A1083337_checkpoint7_Block[][] map;
    //Description : record the cost if you go on the block.
    private HashMap<A1083337_checkpoint7_Block, Integer> accumulatedCost;
    // Description : The route searching algorithm.
    private int algorithm;
    private A1083337_checkpoint7_Fringe fringe;
    private static final int BFS = 1;
    private static final int DFS = 0;
    private static final int UCS = 2;

    //MYSELF
    private A1083337_checkpoint7_Block root;
    public A1083337_checkpoint7_RouteFinder(A1083337_checkpoint7_GameFrame parentFrame, A1083337_checkpoint7_Block target, A1083337_checkpoint7_Block root,int algorithm, A1083337_checkpoint7_Block[][] map){
        /**********************************The TODO This Time (Checkpoint7)**************************
         * 
         * TODO(13): For the TODO here, you have to implement fringe according "algorithm".
         * 
         * Hint(1): The BFS algorithm needs to use the queue to work, so we make a object named BlockQueue for BFS.
         * Hint(2): The DFS algorithm needs to use the stack to work, so we make a object named BlockStack for DFS.
         * Hint(3): The UCS algorithm needs to use the priority queue  to work, so we make a object named PriorityQueue for UCS.
         * Hint(4): These three objects all implement the fringe, and the detail description can be found 
         *          in the code of Fringe.java, BlockQueue.java, BlockStack.java, BlockPriorityQueue.java.
         * Hint(5): You have to add the root (the player current location) into fringe.
         * Hint(6): To calculate the priority, you have to implement a Comparator<block> object and make 
         *          it as an input in the constructor of BlockPriorityQueue.
         * Hint(7): Before starting the searching, you need to initialize the accumulatedCost and set the root with
         *          its cost.
         **********************************The End of the TODO**************************************/
        this.target = target;
        this.ParentBlock = new HashMap<A1083337_checkpoint7_Block, A1083337_checkpoint7_Block>();
        this.parentFrame = parentFrame;
        this.visited = new boolean[4096 / 256][4096 / 256];
        this.accumulatedCost = new HashMap<A1083337_checkpoint7_Block, Integer>();
        this.algorithm = algorithm;
        this.map = map;

        for(int x = 0 ; x < 4096 / 256; x++ ){
            for(int y = 0 ; y < 4096 / 256; y++ ){
                visited[x][y] = false;
            }
        }


        /********************************************************************************************
         START OF YOUR CODE
        ********************************************************************************************/

        accumulatedCost.put(root,root.getCost());

        if(algorithm == BFS){
            A1083337_checkpoint7_BlockQueue queue = new A1083337_checkpoint7_BlockQueue();
            queue.add(root);
            fringe = queue;
        }else if(algorithm == DFS){
            A1083337_checkpoint7_BlockStack stack = new A1083337_checkpoint7_BlockStack();
            stack.add(root);
            fringe = stack;
        }else if(algorithm == UCS){
            A1083337_checkpoint7_BlockPriorityQueue priorityQueue = new A1083337_checkpoint7_BlockPriorityQueue(new Comparator<A1083337_checkpoint7_Block>(){
                @Override
                public int compare(A1083337_checkpoint7_Block a,A1083337_checkpoint7_Block b){
                    return accumulatedCost.get(a) - accumulatedCost.get(b);
                }
            });
            priorityQueue.add(root);
            fringe = priorityQueue;
        }

        /********************************************************************************************
         END OF YOUR CODE
         ********************************************************************************************/
    }
    public A1083337_checkpoint7_Block search(){
        /*********************************The TODO (Checkpoint7)********************************
         * 
         * TODO(14.1): For the TODO here, you have to implement the searching funciton;
         * TODO(14.2): You MUST print the message of "Searching at (x, y)" in order to let us check if you sucessfully do it.
         * TODO(14.3): After you find the target, you just need to return the target block.
         * //System.out.println("Searching at ("+Integer.toString(YOURBLOCK.getX())+", "+Integer.toString(YOURBLOCK.getY())+")");
         * 
         * Hint(1): If the target can not be search you should return null(that means failure).
         * 
         * pseudo code is provided here: 
         *   1. get the block from fringe.
         *   2. print the message
         *   3. if that block equals target return it.
         *   4. if not, expand the block and insert then into fringe.
         *   5. return to 1. until the fringe does not have anyting in it.
         * 
         **********************************The End of the TODO**************************************/
        
        /********************************************************************************************
         START OF YOUR CODE
        ********************************************************************************************/
        ArrayList<A1083337_checkpoint7_Block> expand = new ArrayList<A1083337_checkpoint7_Block>();
        while(!fringe.isEmpty()){
            A1083337_checkpoint7_Block block = fringe.remove();
            System.out.println("Searching at ("+Integer.toString(block.getX())+", "+Integer.toString(block.getY())+")");
            if(block == target){
                return block;
            }else{
                expand = expand(block,ParentBlock,visited);
                for(A1083337_checkpoint7_Block b : expand){
                    if(b.getX() >= 0 && b.getX() <= 15 && b.getY() >= 0 && b.getY() <= 15){
                        fringe.add(b);
                    }
                }
            }
        }
        return null;
        /********************************************************************************************
         END OF YOUR CODE
         ********************************************************************************************/

    }
    private ArrayList<A1083337_checkpoint7_Block> expand(A1083337_checkpoint7_Block currentBlock,HashMap<A1083337_checkpoint7_Block, A1083337_checkpoint7_Block> ParentBlock, boolean[][] visited){
        /*********************************The TODO This Time (Checkpoint7)*****************************
         * 
         * TODO(15.1): For the TODO here, you have to implement the expand funciton and return the Blocks(successor);
         * TODO(15.2): the order that you expand is North(Up) West(Left) South(Down) East(Right).
         * TODO(15.3): before adding the block into successor, you have to check if it is valid by checkBlock().
         * TODO(15.4): For the TODO here, you have to calculate the cost of the path that the player walked from 
         * root to new blocks and set it into the HashMap accumulatedCost.
         * 
         * Hint(1): While the block is valid, before you add the block into successor, 
         *        you should set its ParentBlock (We prepare a HashMap to implement this).
         *        And you should also set it is visited. (We prepare 2D boolean array for you) (the (x,y) block <--> visited[x][y] )
         **********************************The End of the TODO**************************************/

        /********************************************************************************************
         START OF YOUR CODE
        ********************************************************************************************/
        int x = currentBlock.getX();
        int y = currentBlock.getY();
        ArrayList<A1083337_checkpoint7_Block> successor = new ArrayList<A1083337_checkpoint7_Block>();
        if(parentFrame.ClickCheckGridLocation(x,y-1,true) && y-1>=0){
            if(!visited[x][y-1]){
                visited[x][y-1] = true;
                A1083337_checkpoint7_Block visitedBlock = map[x][y-1];
                ParentBlock.put(visitedBlock, currentBlock);
                successor.add(visitedBlock);
                accumulatedCost.put(visitedBlock, accumulatedCost.get(currentBlock)+visitedBlock.getCost());  
            }    
        }
        if(parentFrame.ClickCheckGridLocation(x-1,y,true) && x-1>=0){
            if(!visited[x-1][y]){
                visited[x-1][y] = true;
                A1083337_checkpoint7_Block visitedBlock = map[x-1][y];
                ParentBlock.put(visitedBlock, currentBlock);
                successor.add(visitedBlock);
                accumulatedCost.put(visitedBlock, accumulatedCost.get(currentBlock)+visitedBlock.getCost());    
            }  
        }
        if(parentFrame.ClickCheckGridLocation(x,y+1,true) && y+1<16){
            if(!visited[x][y+1]){
                visited[x][y+1] = true;
                A1083337_checkpoint7_Block visitedBlock = map[x][y+1];
                ParentBlock.put(visitedBlock, currentBlock);
                successor.add(visitedBlock);
                accumulatedCost.put(visitedBlock, accumulatedCost.get(currentBlock)+visitedBlock.getCost());   
            }   
        }
        if(x+1<16 && parentFrame.ClickCheckGridLocation(x+1,y,true) ){
            if(!visited[x+1][y]){
                visited[x+1][y] = true;
                A1083337_checkpoint7_Block visitedBlock = map[x+1][y];
                ParentBlock.put(visitedBlock, currentBlock);
                successor.add(visitedBlock);
                accumulatedCost.put(visitedBlock, accumulatedCost.get(currentBlock)+visitedBlock.getCost());      
            }
        }
        return successor;

        /********************************************************************************************
         END OF YOUR CODE
         ********************************************************************************************/

    }

    public A1083337_checkpoint7_RouteLinkedList createRoute(){
        /******************************The TODO This Time (Checkpoint7)*****************************
         * 
         * TODO(16): For the TODO here, you have to trace back the route and return the route;
         * 
         * Hint1: You can get the parent block of target by HashMap ParentBlock, thus you can calculate
         * the last step of the route. And then you get the parent block of  target, 
         * you can calculate the backward step and so on. 
         * 
         * presudo code is provided here:
         *      1. get parent block
         *      2. calculate the delta location
         *      3. insert into head
         *      4. make the target equals its parent block and so on.
         * 
         **********************************The End of the TODO**************************************/

        /********************************************************************************************
         START OF YOUR CODE
        ********************************************************************************************/
        A1083337_checkpoint7_RouteLinkedList linkedList = new A1083337_checkpoint7_RouteLinkedList();
        A1083337_checkpoint7_Block des = this.search();
        A1083337_checkpoint7_Block parentBlock = ParentBlock.get(des);
        int desX=-1,desY=-1,pX=0,pY=0,axis=-1,dir=0;
       
        if(des != null){
            while(parentBlock != root){
                
                desX = des.getX();
                desY = des.getY();            
                if(parentBlock != null){
                    pX = parentBlock.getX();
                    pY = parentBlock.getY();           
                    if(desX-pX == 0){
                        axis = 1;
                    }
                    if(desY-pY == 0){
                        axis = 0;
                    }
                    if(axis == 1){
                        dir = (desY-pY < 0) ? -1 : 1; 
                    }
                    if(axis == 0){
                        dir = (desX-pX < 0) ? -1 : 1;
                    }
                }

                if (linkedList.getHead() == null) {
                    linkedList.append(axis, dir);
                } else {
                    int referenceaxis = linkedList.getHead().getAxis();
                    int referencedir = linkedList.getHead().getDirection();
                    linkedList.insert(referenceaxis, referencedir, axis, dir);
                }

                des = parentBlock;
                parentBlock = ParentBlock.get(des);
            }
        }

        return linkedList; 
        /********************************************************************************************
         END OF YOUR CODE
         ********************************************************************************************/
    }

    
}
