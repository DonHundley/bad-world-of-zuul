/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, lobby, lockers, theatre, office, stairs, hallway, upstairs, basement, downstairs, purgatory;
      
        // create the rooms
        outside = new Room("""
                at the school in your Mom's car. It is saturday and you're here for theatre practice.
                Normally you walk but you were running late and it is raining heavily.
                You look over and your Mom smiles and tells you to have fun. You quickly thank her and get out of the car.
                As you step out the door there is a large puddle, your shoes are soaked!.
                Good thing you have your costume shoes.""");
        lobby = new Room("""
                in the lobby, still soaked from rain. You need to change and get to practice fast. 
                There is an office to your east, a stairway is to your north, and a hallway to your west.""");
        stairs = new Room("in the stairwell");
        lockers = new Room("""
                in the locker room. You still feel foggy, why are you here again?
                You sit and think, trying to remember...
                Right! You need to change into your costume. You run to your locker and open it.
                Inside you see a pile of wet clothes, a little shocked you look down.
                You're wearing your outfit! Today really just isn't your day.
                Get to the theatre!""");
        office = new Room("""
                in the office lobby. No one is here, but there is a layout of the school. 
                There is a locker room on the lower floor, the theatre class room is upstairs, 
                and the actual theatre is at the end of the hallway on the bottom floor.""");
        hallway = new Room("""
                in the main floor hallway. There is just a hall of classrooms.
                I'm not sure this is where I'm supposed to be, maybe I took a wrong turn in the rush.""");
        upstairs = new Room("""
                in the upstairs hallway.
                Your friend was supposed to be in the theatre classroom waiting on you,
                but you can see it is empty.
                They must be downstairs with the class already. I should go.""");
        downstairs = new Room("""
                rushing down the stairs. You can't believe you're late again!
                Your wet shoes slip and you fall backwards. You feel yourself land hard.
                You're dazed but you don't hurt, so you hop up and continue running to the basement.""");
        basement = new Room("""
                in the basement.
                The locker room is directly to your east and the theatre stage entrance
                is at the end of the west hallway.""");
        theatre = new Room("""
                finally in the theatre, your favorite place to be.
                You breathe a sigh of relief.
                You always enjoy being on stage with your friends,
                there is no doubt you would do this everyday for the rest of eternity.
                You wonder however, where is everyone? your friends aren't around.
                Was there practice today? Was I that late?
                The more you think about it the less you remember.
                The stage lights go dim. Why is it so hard to think?
                You try and recall the first thing that comes to mind from today..
                Your Mom! Maybe she is still outside? I could call her or go back outside.""");
        purgatory = new Room ("""
                in a dark hallway. Who are you? Why do you feel so cold? Alone?
                You just hear a faint sobbing, it chills you to your core. 
                It sounds so familiar to you. Mom! Moooom! Anyone?!
                You call out, the hallway echoes your voice back. 
                Something doesn't feel right, your world feels like its shrinking.
                You should move.""");
        // initialise room exits
        outside.setExit("north", lobby);
        lobby.setExit("north", stairs);
        lobby.setExit("east", office);
        lobby.setExit("west", hallway);
        stairs.setExit("up", upstairs);
        stairs.setExit("down", downstairs);
        hallway.setExit("east",lobby);
        downstairs.setExit("down", basement);
        basement.setExit("east", lockers);
        basement.setExit("west", theatre);
        upstairs.setExit("down", stairs);
        office.setExit("west", lobby);
        theatre.setExit("east", purgatory);
        purgatory.setExit("north", outside);
        purgatory.setExit("east", outside);
        purgatory.setExit("south", outside);
        purgatory.setExit("west", outside);
        purgatory.setExit("up", outside);
        purgatory.setExit("down", outside);
        lockers.setExit("west", basement);
        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to theatre practice!");
        System.out.println("Theatre practice is a story game about one theatre student's day.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if(commandWord.equals("look")) {
            look();
        }
        else if(commandWord.equals("callmom")) {
            callMom();
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Your goal is to make it through the day.");
        System.out.println("You're running late so you should be quick, the other students are likely waiting on you.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Allows the player to remind themselves of their surroundings.
     */
    private void look()
    {
        System.out.println(currentRoom.getLongDescription());
    }

    private void callMom()
    {
        System.out.println("""
                You take out your phone and call your mom.
                Odd, there is no ring tone. All you hear is... sobbing?
                Mom doesn't answer. She is probably driving anyway and you
                wouldn't want to risk an accident.
                You hang up and put your phone away.""");
    }

    private void printLocationInfo()
    {
        System.out.println(currentRoom.getLongDescription());
        System.out.println();
    }
}
