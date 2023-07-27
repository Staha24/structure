package Seminar04;

public class BinTree<T extends Comparable<T>> {
    Node root;
    private int size;

    public boolean add(T value) {//добавление значений
        if (root == null) {
            root = new Node(value);
            root.color = Color.black;//при создании рута цвет обязательно должен быть черным
            size = 1;
            return true;
        }
        if (addNode(root, value) != null) {
            size++;
            return true;
        }
        return false;
    }

    private Node addNode(Node node, T value) {//добавление элементов через рекурсию, т.е. node это начало,
        //value это текущее значение, если нода меньше значения, то идем влево и наоборот
        if (node.value.compareTo(value) == 0)
            return null;
        if (node.value.compareTo(value) > 0) { // left
            if (node.left == null) {
                node.left = new Node(value);
                return node.left;
            }
            Node result = addNode(node.left, value);
            node.left = rebalance(node.left);
            return result;
        } else { // right
            if (node.right == null) {
                node.right = new Node(value);
                return node.right;
            }
            Node result = addNode(node.right, value);
            node.right = rebalance(node.right);
            return result;
        }
    }

    public boolean remove(T value) {//удаление значения, вместо удаляемого ставится ближайшее большее значение
        Node deleteNode = root;
        while (deleteNode != null) {
            if (deleteNode.value.compareTo(value) == 0) {
                if (deleteNode.right != null) {
                    Node currentNode = deleteNode.right;
                    Node prevCurrentNode = deleteNode.right;
                    while (currentNode.left != null)
                        if (currentNode == prevCurrentNode)
                            currentNode = currentNode.left;
                        else {
                            currentNode = currentNode.left;
                            prevCurrentNode = prevCurrentNode.left;
                        }
                    deleteNode.value = currentNode.value;
                    prevCurrentNode.left = null;
                    rebalance(deleteNode);
                    return true;
                }
            }
            if (deleteNode.value.compareTo(value) > 0)
                deleteNode = deleteNode.left;
            else
                deleteNode = deleteNode.right;
        }
        return false;
    }

    private Node rebalance(Node node) {//будет возвращать ноду
        Node result = node;//резалт, это та нода, на которой мы начали(сохраняется ее место)
        boolean needRebalance = true;//это для проерки
        while (needRebalance) {
            needRebalance = false;
            if (result.right != null && result.right.color == Color.red
                    && (result.left == null || result.left.color == Color.black)) {
                needRebalance = true;
                result = rightSwap(result);
            }
            if (result.left != null && result.left.color == Color.red
                    && result.left.left != null && result.left.left.color == Color.red) {
                needRebalance = true;
                result = leftSwap(result);
            }
            if (result.left != null && result.right != null
                    && result.left.color == Color.red && result.right.color == Color.red) {
                needRebalance = true;
                colorSwap(result);
            }
        }
        return result;
    }

    private void colorSwap(Node node) {//происходит тогда, когда обы наследника красные, т.е. наследники становятся черными, а родитель красным
        node.right.color = Color.black;
        node.left.color = Color.black;
        node.color = Color.red;
    }

    private Node leftSwap(Node node) {//проверяется правая нода и происходит смещение
        Node left = node.left;
        Node between = left.right;
        left.right = node;
        node.left = between;
        left.color = node.color;
        node.color = Color.red;
        return left;
    }

    private Node rightSwap(Node node) {//наоборот, черная справа родитель, слева красная
        Node right = node.right;
        Node between = right.left;
        right.left = node;
        node.right = between;
        right.color = node.color;
        node.color = Color.red;
        return right;
    }


    private class Node {//создали ноду
        T value;
        Node left;
        Node right;
        Color color;

        Node() {
            color = Color.red;//добавили цвет ноде
        }

        Node(T value) {
            this.value = value;
            color = Color.red;//добавили цвет ноде
        }
    }


    private class PrintNode {
        Node node;
        String str;
        int depth;

        public PrintNode() {
            node = null;
            str = " ";
            depth = 0;
        }

        public PrintNode(Node node) {
            depth = 0;
            this.node = node;
            this.str = node.value.toString();
        }
    }

    public void print() {

        int maxDepth = maxDepth() + 3;
        int nodeCount = nodeCount(root, 0);
        int width = 50;//maxDepth * 4 + 2;
        int height = nodeCount * 5;
        List<List<PrintNode>> list = new ArrayList<List<PrintNode>>();
        for (int i = 0; i < height; i++) /*РЎРѕР·РґР°РЅРёРµ СЏС‡РµРµРє РјР°СЃСЃРёРІР°*/ {
            ArrayList<PrintNode> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new PrintNode());
            }
            list.add(row);
        }

        list.get(height / 2).set(0, new PrintNode(root));
        list.get(height / 2).get(0).depth = 0;

        for (int j = 0; j < width; j++)  /*РџСЂРёРЅС†РёРї Р·Р°РїРѕР»РЅРµРЅРёСЏ*/ {
            for (int i = 0; i < height; i++) {
                PrintNode currentNode = list.get(i).get(j);
                if (currentNode.node != null) {
                    currentNode.str = currentNode.node.value.toString();
                    if (currentNode.node.left != null) {
                        int in = i + (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.left;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;

                    }
                    if (currentNode.node.right != null) {
                        int in = i - (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.right;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;
                    }

                }
            }
        }
        for (int i = 0; i < height; i++) /*Р§РёСЃС‚РєР° РїСѓСЃС‚С‹С… СЃС‚СЂРѕРє*/ {
            boolean flag = true;
            for (int j = 0; j < width; j++) {
                if (list.get(i).get(j).str != " ") {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.remove(i);
                i--;
                height--;
            }
        }

        for (var row : list) {
            for (var item : row) {
                System.out.print(item.str + " ");
            }
            System.out.println();
        }
    }

    /*private void printLines(List<List<PrintNode>> list, int i, int j, int i2, int j2) {
        if (i2 > i) // РРґС‘Рј РІРЅРёР·
        {
            while (i < i2) {
                i++;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "\\";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        } else {
            while (i > i2) {
                i--;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "/";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        }
    }*/

    public int maxDepth() {
        return maxDepth2(0, root);
    }

    private int maxDepth2(int depth, Node node) {
        depth++;
        int left = depth;
        int right = depth;
        if (node.left != null)
            left = maxDepth2(depth, node.left);
        if (node.right != null)
            right = maxDepth2(depth, node.right);
        return left > right ? left : right;
    }

    private int nodeCount(Node node, int count) {
        if (node != null) {
            count++;
            return count + nodeCount(node.left, 0) + nodeCount(node.right, 0);
        }
        return count;
    }

    enum Color {black, red}//добавили в дерево цвет

}
