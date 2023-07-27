package Seminar04;

public class Main {
    public static void main(String[] args) {

        /*String str2 = "A";
        String str3 = "D";
        String str4 = "J";
        String str5 = "";


        /*System.out.println(str2.hashCode() % 4);
        System.out.println(str3.hashCode() % 4);
        System.out.println(str4.hashCode() % 4);
        System.out.println();
        System.out.println();
        System.out.println(str4.hashCode() % 4);*/

        /*HashTable<String,Integer> table = new HashTable<>();
        table.add("A",5);
        table.add("B",3);
        table.add("C",8);
        table.add("J",6);
        table.add("L",2);
        table.add("F",1);

        table.print();*/


        BinTree<Integer> tree = new BinTree<>();
        tree.add(5);
        tree.add(1);
        tree.add(9);
        tree.add(8);
        tree.add(4);
        tree.add(7);
        tree.add(6);

        tree.remove(5);

        tree.print();

    }
    
}
