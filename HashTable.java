package Seminar04;

public class HashTable<K, V> {//сама хеш таблица, в кот.задается размер
    private static final int DEFAULT_SIZE = 4;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Bucket<K, V>[] buckets;

    public HashTable() {
        this(DEFAULT_SIZE);
    }

    public HashTable(int _size) {//сама хеш таблица, в кот.проверяется размер
        if (_size <= DEFAULT_SIZE)
            buckets = new Bucket[DEFAULT_SIZE];
        else
            buckets = new Bucket[_size];
        size = 0;
    }

    private int calculateIndex(K key) {//ф-ция высчитывания индекса
        return Math.abs(key.hashCode() % buckets.length);
    }

    public boolean add(K key, V value) {//добавление элемента
        if (buckets.length * LOAD_FACTOR < size)//проверяем длину массива
            resize();
        int index = calculateIndex(key);//берем и записываем индекс
        Bucket<K, V> bucket = buckets[index];//забираем темповое ведро и проверяем его
        if (bucket == null) {
            bucket = new Bucket<>();
            buckets[index] = bucket;
        }
        boolean added = bucket.add(key, value);
        if (added)
            size++;
        return added;
    }

    public boolean remove(K key) {
        int index = calculateIndex(key);
        Bucket<K, V> bucket = buckets[index];
        if (bucket == null)
            return false;
        boolean removed = bucket.remove(key);
        if (removed)
            size--;
        return removed;
    }

    public void print() {
        for (var item : buckets) {
            if (item != null) {
                item.print();
                System.out.println();
            } else
                System.out.println("---");
        }
    }

    private void resize() {
        Bucket<K, V>[] old = buckets;
        buckets = new Bucket[old.length * 1];
        for (int i = 0; i < old.length; i++) {
            Bucket<K, V> bucket = old[i];
            if (bucket == null)
                continue;
            Bucket.Node currentNode = bucket.root;
            while (currentNode != null) {
                this.add((K) currentNode.pair.key, (V) currentNode.pair.value);
                currentNode = currentNode.next;
            }
            old[i] = null;
        }
        old = null;
    }

    private class Bucket<K, V> {
        Node root;

        private boolean add(Pair pair) {//добавление ноды
            Node newNode = new Node();
            newNode.pair = pair;
            if (root == null) {
                root = newNode;
                return true;
            }
            Node currentNode = root;
            while (currentNode != null) {
                if (currentNode.pair.key.equals(pair.key))
                    return false;
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    return true;
                }
                currentNode = currentNode.next;
            }
            return false;
        }

        public boolean add(K key, V value) {//добавление ключ-значение
            Pair pair = new Pair(key, value);
            return this.add(pair);
        }

        public boolean remove(K key) {//удаление по ключу
            if (root == null) return false;
            if (root.pair.key.equals(key)) {
                root = root.next;
                return true;
            }
            Node currentNode = root;
            while (currentNode.next != null) {
                if (currentNode.next.pair.key.equals(key)) {
                    currentNode.next = currentNode.next.next;
                    return true;
                }
                currentNode = currentNode.next;
            }
            return false;
        }

        public V getValue(K key) {//получение значения по ключу
            Node currentNode = root;
            while (currentNode != null) {
                if (currentNode.pair.key.equals(key))
                    return currentNode.pair.value;
                currentNode = currentNode.next;
            }
            return null;
        }

        public boolean set(K key, V value) {//установка значения по ключу
            Node currentNode = root;
            while (currentNode != null) {
                if (currentNode.pair.key.equals(key)) {
                    currentNode.pair.value = value;
                    return true;
                }
                currentNode = currentNode.next;
            }
            return false;
        }

        public void print() {//печать всех значений
            Node currentNode = root;
            while (currentNode != null) {
                System.out.print("[" + currentNode.pair.key + ";" + currentNode.pair.value + "]");
                currentNode = currentNode.next;
            }
        }

        private class Node {//класс Нода для списка
            Pair pair;
            Node next;
        }

        private class Pair { //класс для хранения пар
            K key;
            V value;

            public Pair() {
            }

            public Pair(K _key, V _value) {
                this.key = _key;
                this.value = _value;
            }
        }
    }
}
