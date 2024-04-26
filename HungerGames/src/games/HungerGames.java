package games;

import java.util.ArrayList;

/**
 * This class contains methods to represent the Hunger Games using BSTs.
 * Moves people from input files to districts, eliminates people from the game,
 * and determines a possible winner.
 * 
 * @author Pranay Roni
 * @author Maksims Kurjanovics Kravcenko
 * @author Kal Pandit
 */
public class HungerGames {

    private ArrayList<District> districts; // all districts in Panem.
    private TreeNode game; // root of the BST. The BST contains districts that are still in the game.

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Default constructor, initializes a list of districts.
     */
    public HungerGames() {
        districts = new ArrayList<>();
        game = null;
        StdRandom.setSeed(2023);
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Sets up Panem, the universe in which the Hunger Games takes place.
     * Reads districts and people from the input file.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPanem(String filename) {
        StdIn.setFile(filename); // open the file - happens only once here
        setupDistricts(filename);
        setupPeople(filename);
    }

    /**
     * Reads the following from input file:
     * - Number of districts
     * - District ID's (insert in order of insertion)
     * Insert districts into the districts ArrayList in order of appearance.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupDistricts(String filename) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(filename);

        // reads first line
        String firstLine = StdIn.readLine();

        // A number n on the first line specifies the amount of districts.
        int n = Integer.parseInt(firstLine);

        // Loop runs n times and reads n lines (it does not read the first line because
        // that has already been read)
        for (int i = 1; i <= n; i++) {
            // Stores each of the ID's in order of apperance
            String currentLine = StdIn.readLine();
            int currentId = Integer.parseInt(currentLine);

            // Creates a District object for each ID and adds them into the districts
            // Arraylist
            District current = new District(currentId);
            districts.add(current);
        }
    }

    /**
     * Reads the following from input file (continues to read from the SAME input
     * file as setupDistricts()):
     * Number of people
     * Space-separated: first name, last name, birth month (1-12), age, district id,
     * effectiveness
     * Districts will be initialized to the instance variable districts
     * 
     * Persons will be added to corresponding district in districts defined by
     * districtID
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPeople(String filename) {

        // WRITE YOUR CODE HERE
        int p = Integer.parseInt(StdIn.readLine());

        for (int i = 1; i <= p; i++) {
            String[] data = StdIn.readLine().split(" ");

            // Data about people
            String firstName = data[0];
            String lastName = data[1];
            int birthMonth = Integer.parseInt(data[2]);
            int age = Integer.parseInt(data[3]);
            int districtID = Integer.parseInt(data[4]);
            int effectiveness = Integer.parseInt(data[5]);

            Person current = new Person(birthMonth, firstName, lastName, age, districtID, effectiveness);

            if (current.getAge() >= 12 && current.getAge() < 18)
                current.setTessera(true);

            for (District respecDis : districts) {
                if (respecDis.getDistrictID() == districtID) {
                    if (current.getBirthMonth() % 2 == 0) {
                        respecDis.addEvenPerson(current);
                    } else {
                        respecDis.addOddPerson(current);
                    }
                }
            }

        }

    }

    /**
     * Adds a district to the game BST.
     * If the district is already added, do nothing
     * 
     * @param root        the TreeNode root which we access all the added districts
     * @param newDistrict the district we wish to add
     */
    public void addDistrictToGame(TreeNode root, District newDistrict) {

        // WRITE YOUR CODE HERE

        // Create a new TreeNode containing the new District
        TreeNode tobeInserted = new TreeNode(newDistrict, null, null);

        if (root == null) {
            game = tobeInserted;
            districts.remove(newDistrict);
            return;
        }

        TreeNode currentNode = root;

        do {
            if (tobeInserted.getDistrict().getDistrictID() > currentNode.getDistrict().getDistrictID()) {
                if (currentNode.getRight() == null) {
                    currentNode.setRight(tobeInserted);
                    districts.remove(newDistrict);
                } else {
                    currentNode = currentNode.getRight();
                }
            } else if (tobeInserted.getDistrict().getDistrictID() < currentNode.getDistrict().getDistrictID()) {
                if (currentNode.getLeft() == null) {
                    currentNode.setLeft(tobeInserted);
                    districts.remove(newDistrict);
                } else {
                    currentNode = currentNode.getLeft();
                }
            } else {
                return;
            }
        } while (currentNode.getLeft() == null || currentNode.getRight() == null);

    }

    /**
     * Searches for a district inside of the BST given the district id.
     * 
     * @param id the district to search
     * @return the district if found, null if not found
     */
    public District findDistrict(int id) {

        // WRITE YOUR CODE HERE
        return findDistrict(game, id);

        // update this line
    }

    private District findDistrict(TreeNode node, int id) {
        if (node == null)
            return null;

        if (id > node.getDistrict().getDistrictID())
            return findDistrict(node.getRight(), id);

        else if (id < node.getDistrict().getDistrictID())
            return findDistrict(node.getLeft(), id);

        else
            return node.getDistrict();

    }

    /**
     * Selects two duelers from the tree, following these rules:
     * - One odd person and one even person should be in the pair.
     * - Dueler with Tessera (age 12-18, use tessera instance variable) must be
     * retrieved first.
     * - Find the first odd person and even person (separately) with Tessera if they
     * exist.
     * - If you can't find a person, use StdRandom.uniform(x) where x is the
     * respective
     * population size to obtain a dueler.
     * - Add odd person dueler to person1 of new DuelerPair and even person dueler
     * to
     * person2.
     * - People from the same district cannot fight against each other.
     * 
     * @return the pair of dueler retrieved from this method.
     */
    public DuelPair selectDuelers() {
        // WRITE YOUR CODE HERE
        DuelPair duelers = new DuelPair(null, null);
        findOddWithTessera(game, duelers);

        District district = null;
        if (duelers.getPerson1() != null) {
            district = findDistrict(game, duelers.getPerson1().getDistrictID());
        }
        findEvenWithTessera(game, duelers, district);

        if (duelers.getPerson1() == null) {
            District find = null;
            if (duelers.getPerson2() != null) {
                find = findDistrict(game, duelers.getPerson2().getDistrictID());
            }
            findRandomOdd(game, duelers, find);
        }

        if (duelers.getPerson2() == null)
            findRandomEven(game, duelers, findDistrict(game, duelers.getPerson1().getDistrictID()));
        return duelers; // update this line
    }

    private void findOddWithTessera(TreeNode root, DuelPair duelers) {
        if (root == null || duelers.getPerson1() != null)
            return;
        ArrayList<Person> odd = root.getDistrict().getOddPopulation();
        for (Person person : odd) {
            if (person.getTessera()) {
                duelers.setPerson1(person);
                odd.remove(person); // Consider modifying this
                break;
            }
        }
        findOddWithTessera(root.getLeft(), duelers);
        findOddWithTessera(root.getRight(), duelers);
    }

    private void findEvenWithTessera(TreeNode root, DuelPair duelers, District previousDistrict) {
        if (root == null || duelers.getPerson2() != null) {
            return;
        }
        if (root.getDistrict() != previousDistrict) {
            ArrayList<Person> even = root.getDistrict().getEvenPopulation();
            for (Person person : even) {
                if (person.getTessera()) {
                    duelers.setPerson2(person);
                    even.remove(person);
                    break;
                }
            }
        }
        findEvenWithTessera(root.getLeft(), duelers, previousDistrict);
        findEvenWithTessera(root.getRight(), duelers, previousDistrict);
    }

    private void findRandomOdd(TreeNode root, DuelPair duelers, District previousDistrict) {
        if (duelers.getPerson1() != null || root == null) {
            return;
        }
        ArrayList<Person> odd = root.getDistrict().getOddPopulation();
        int x = odd.size();
        int random = StdRandom.uniform(x);
        int i = 0;
        if (root.getDistrict() != previousDistrict) {
            for (Person person : odd) {
                if (i == random) {
                    duelers.setPerson1(person);
                    odd.remove(person);
                    break;
                }
                i++;
            }
        }
        findRandomOdd(root.getLeft(), duelers, previousDistrict);
        findRandomOdd(root.getRight(), duelers, previousDistrict);
    }

    private void findRandomEven(TreeNode node, DuelPair duelers, District previousDistrict) {
        if (duelers.getPerson2() != null || node == null) {
            return;
        }
        if (node.getDistrict() != previousDistrict) {
            ArrayList<Person> even = node.getDistrict().getEvenPopulation();
            int x = even.size();
            int random = StdRandom.uniform(x);
            int i = 0;
            for (Person person : even) {
                if (i == random) {
                    duelers.setPerson2(person);
                    even.remove(person);
                    break;
                }
                i++;
            }
        }
        findRandomEven(node.getLeft(), duelers, previousDistrict);
        findRandomEven(node.getRight(), duelers, previousDistrict);
    }

    /**
     * Deletes a district from the BST when they are eliminated from the game.
     * Districts are identified by id's.
     * If district does not exist, do nothing.
     * 
     * This is similar to the BST delete we have seen in class.
     * 
     * @param id the ID of the district to eliminate
     */
    public void eliminateDistrict(int id) {


        TreeNode toBeDeleted = findNode(game, id);
        
        if (toBeDeleted == null)
            return;

        game = eliminateDistrict(game, id);
        // WRITE YOUR CODE HERE
    }

    private TreeNode findNode(TreeNode node, int id) {
        if (node == null)
            return null;

        if (id > node.getDistrict().getDistrictID())
            return findNode(node.getRight(), id);

        else if (id < node.getDistrict().getDistrictID())
            return findNode(node.getLeft(), id);

        else
            return node;
    }

    private TreeNode eliminateDistrict(TreeNode x, int id) {

        if (x == null)
            return null;

        int cmp = id - x.getDistrict().getDistrictID();

        if (cmp < 0)
            x.setLeft(eliminateDistrict(x.getLeft(), id));

        else if (cmp > 0)
            x.setRight(eliminateDistrict(x.getRight(), id));

        else {
            if (x.getRight() == null)
                return x.getLeft();
            if (x.getLeft() == null)
                return x.getRight();

            TreeNode t = x;
            x = min(t.getRight());
            x.setRight(deleteMin(t.getRight()));
            x.setLeft(t.getLeft());
        }
        return x;
    }

    private TreeNode deleteMin(TreeNode x) {
        if (x.getLeft() == null)
            return x.getRight();
        x.setLeft(deleteMin(x.getLeft()));
        return x;
    }

    private TreeNode min(TreeNode node) {
        TreeNode current = node;

        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

   
    /**
     * Eliminates a dueler from a pair of duelers.
     * - Both duelers in the DuelPair argument given will duel
     * - Winner gets returned to their District
     * - Eliminate a District if it only contains a odd person population or even
     * person population
     * 
     * @param pair of persons to fight each other.
     */
    public void eliminateDueler(DuelPair pair) {

        // WRITE YOUR CODE HERE
        /*
         * If the pair is Incomplete (there is only one person in the pair):
         * return the person in the pair back to the district they came from, to their
         * respective odd or even population.
         * Do nothing else if this is the case.
         */
        if (pair.getPerson1() != null && pair.getPerson2() == null
                || pair.getPerson1() == null && pair.getPerson2() != null) {
            if (pair.getPerson1() != null) {
                District reAddOdd = findDistrict(pair.getPerson1().getDistrictID());
                reAddOdd.addOddPerson(pair.getPerson1());
                return;
            }
            District reAddEven = findDistrict(pair.getPerson2().getDistrictID());
            reAddEven.addEvenPerson(pair.getPerson2());
            return;
        }

        /*
         * Use the duel() method built into the Person class to have two duelers fight
         * against each other. For example, calling person1.duel(person2) will return a
         * winner; the other person will be the loser.
         */

        Person winner = pair.getPerson1().duel(pair.getPerson2());

        Person loser = null;

        if (winner == pair.getPerson1()) {
            loser = pair.getPerson2();
        } else if (winner == pair.getPerson2()) {
            loser = pair.getPerson1();
        }

        District winna = findDistrict(winner.getDistrictID());
        District losa = findDistrict(loser.getDistrictID());
        int idWin = winner.getDistrictID();
        int idLos = loser.getDistrictID();

        if (winner.getBirthMonth() % 2 == 0) {
            winna.addEvenPerson(winner);
        } else {
            winna.addOddPerson(winner);
        }

        // Check if the districts of the loser or winner are limited

        if (winna.getEvenPopulation().size() == 0 || winna.getOddPopulation().size() == 0) {
            eliminateDistrict(idWin);
        }

        if (losa.getEvenPopulation().size() == 0 || losa.getOddPopulation().size() == 0) {
            eliminateDistrict(idLos);
        }

    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Obtains the list of districts for the Driver.
     * 
     * @return the ArrayList of districts for selection
     */
    public ArrayList<District> getDistricts() {
        return this.districts;
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Returns the root of the BST
     */
    public TreeNode getRoot() {
        return game;
    }
}
