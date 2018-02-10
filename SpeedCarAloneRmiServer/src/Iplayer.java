
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author brasc
 */
public abstract class Iplayer {

    /**
     * Un random normalement plus sécurisé que le Random classique, Il est
     * réputé non devinable.
     *
     */
    private static final SecureRandom prng = new SecureRandom();

    /**
     * Nom du client choisi à la connexion
     */
    private final String name;
    
    /**
     * Score du client
     */
    private int score = 0;

    /**
     * Utiliser le Random pour generer un id unisque au client
     */
    private final long userId = prng.nextLong();

    /**
     * Pour enregistrer la partie à laquelle le client appartient
     */
    private Core core = null;
    
    
    private boolean bGameInProgress = false;

    /**
     * Constructeur
     * @param name 
     */
    protected Iplayer(String name) {
        this.name = name;
      
    }

    /**
     * 
     * @param vDisplayRoad
     * @param vDisplayObstacles
     * @param vDisplayCars
     * @param myCar
     * @param pos
     * @param nbParticipants
     * @param bGameOver
     * @param sPosition
     * @throws java.rmi.RemoteException 
     */
    public abstract void update(Vector<Rectangle> vDisplayRoad, Vector<Rectangle> vDisplayObstacles, Vector<Rectangle> vDisplayCars, Car myCar, int pos, int nbParticipants, boolean bGameOver, String sPosition) throws java.rmi.RemoteException;

  
    public abstract void setPlayerButton(boolean flag);

    /**
     * Retourne l'id unique du client
     *
     * @return
     */
    public long getId() {
        return this.userId;
    }

    public String getName() {
        return name;
    }

    /**
     * Fait entrer le client dans une arena
     * @param core
     * @return 
     */
    public boolean setCore(Core core) {
        if (core != null || this.core == null) {
            this.core = core;
            return true;
        }
        return false;
    }

    public Core getCore() {
        return core;
    }

    /**
     * 
     * @return 
     */
    public synchronized int getScore() {
        return score;
    }

    /**
     * 
     * @param score 
     */
    public synchronized void setScore(int score) {    
        this.score = score;
    }

   

        /**
     * Moves the cars according to their speed, acceleration and key pressed for
     * the player's car
     *
     * @param UP_P True if the up arrow is being pressed
     * @param DO_P True if the down arrow is being pressed
     * @param LE_P True if the left arrow is being pressed
     * @param RI_P True if the right arrow is being pressed
     * @param vCars The vector of cars to move
     */
    public synchronized void moveCars(boolean bGameFinishing,boolean flag,boolean UP_P, boolean DO_P, boolean LE_P, boolean RI_P, Vector<Car> vCars) {
        //Extract the player's car (always at position 0 in the vector!)
        Car myCar = vCars.elementAt(0);

        //If we did not pass the finish line, we can still act on the acceleration on the y axis
        if (!bGameFinishing) {
            if (UP_P) {
                //Accelerates
                if (myCar.yAcc < 4) {
                    myCar.yAcc++;
                }
            } else if (DO_P) {
                //Decelerates
                if (myCar.yAcc > -8) {
                    myCar.yAcc--;
                }
            } else //Iteratively reachs a constant deceleration of -1 if no key is pressed
             if (myCar.yAcc > -1) {
                    myCar.yAcc--;
                } else if (myCar.yAcc < -1) {
                    myCar.yAcc++;
                }
        } else {
            //If we passed the finish line, we must decelerate
            myCar.yAcc = -8;
        }

        //Impacts the acceleration of the x axis
        if (RI_P) {
            //Going to the right
            if (myCar.xAcc < 4) {
                myCar.xAcc++;
            }
        } else if (LE_P) {
            //Going to the left
            if (myCar.xAcc > -4) {
                myCar.xAcc--;
            }
        } else //If we don't press anything, the x acceleration is calculated to iteratively counter the x speed and make it reach 0
         if (myCar.xSpeed > 1) {
                myCar.xAcc = -(int) (myCar.xSpeed + 1);
                if (myCar.xAcc < -4) {
                    myCar.xAcc = -4;
                }
            } else if (myCar.xSpeed < -1) {
                myCar.xAcc = -(int) (myCar.xSpeed - 1);
                if (myCar.xAcc > 4) {
                    myCar.xAcc = 4;
                }
            } else {
                myCar.xAcc = 0;
                myCar.xSpeed = 0;
            }

        //We then scan the other cars
        Iterator<Car> iCars = vCars.iterator();
        while (iCars.hasNext()) {
            Car currentCar = iCars.next();

            //If this is not the player's car
            if (currentCar.id != 6) {
                //We try to maintain the x speed to 0 using the same formula
                if (currentCar.xSpeed > 1) {
                    currentCar.xAcc = -(int) (currentCar.xSpeed + 1);
                } else if (currentCar.xSpeed < -1) {
                    currentCar.xAcc = -(int) (currentCar.xSpeed - 1);
                } else {
                    currentCar.xAcc = 0;
                    currentCar.xSpeed = 0;
                }
            }

            //The speed on the y axis is updated according to the acceleration
            currentCar.ySpeed += (double) currentCar.yAcc / 100;

            //We try to maintain the car speed in its acceptable range of functionning
            if (currentCar.ySpeed < 0.5) {
                //The car must have at least a speed of 0.5
                if (!bGameFinishing) {
                    currentCar.ySpeed = 0.5;
                } else //Unless the player passes the finish line which, at this point, stops the game
                 if (currentCar.id == 6) {
                        currentCar.ySpeed = 0;
                        bGameInProgress = flag;
                    } else {
                        currentCar.ySpeed = 0.5;
                    }
            } //The opponents will try to stay at 3.58
            else if (currentCar.ySpeed > 3.5 && currentCar.Racer && currentCar.id == 7) {
                currentCar.ySpeed = (currentCar.ySpeed - 3.5) * 0.8 + 3.5;
            } //The player car cannot exceed a speed of 4.64
            else if (currentCar.ySpeed > 4.5 && currentCar.Racer && currentCar.id == 6) {
                currentCar.ySpeed = (currentCar.ySpeed - 4.5) * 0.8 + 4.5;
            } //The civilans will try to stay at 2.04
            else if (currentCar.ySpeed > 2 && !currentCar.Racer) {
                currentCar.ySpeed = (currentCar.ySpeed - 2) * 0.8 + 2;
            }

            //The speed on the x axis is updated with the acceleration
            currentCar.xSpeed += (double) currentCar.xAcc / 5;

            //The speed will stay in a certain range [-8,8]
            if (currentCar.xSpeed > 8) {
                currentCar.xSpeed = 8;
            }
            if (currentCar.xSpeed < -8) {
                currentCar.xSpeed = -8;
            }

            //We update the car position according to its speeds
            currentCar.y -= currentCar.ySpeed * 4;
            currentCar.x += currentCar.xSpeed;

            //But the position of the car must be in the range [0,368]
            if (currentCar.x < 0) {
                currentCar.x = 0;
                currentCar.xAcc = 0;
                currentCar.xSpeed = 0;
            }
            if (currentCar.x > 368) {
                currentCar.x = 368;
                currentCar.xAcc = 0;
                currentCar.xSpeed = 0;
            }

            //If, for some reason, the player car did not decrease enough after the finish line, we ensure that the game will end
            //before we get out of the visible road (+ a safety margin)
            if (bGameFinishing && currentCar.id == 6 && currentCar.y < 500) {
                bGameInProgress = flag;
            }

        }
    }
   
}
