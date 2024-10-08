package com.pseudoyu.tree.rb;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;

// 定义红黑树类
public class RedBlackTree<Key extends Comparable<Key>, Value> {

    // 定义红黑树中节点的颜色
    // 使用布尔值来表示两种颜色：红色为 true，黑色为 false
    protected static final boolean RED = true;
    protected static final boolean BLACK = false;


    // 定义红黑树的根节点
    // 使用 Node 类型，Node 是红黑树中的节点类型
    private Node root;      // 根节点

    /**
     * 初始化一个空的符号表（红黑树）。
     */
    public RedBlackTree() {
        // 构造函数体为空，因为根节点默认为 null
    }

    /**
     * 获取红黑树的根节点。
     *
     * @return 根节点
     */
    protected Node getRoot() {
        return root;
    }

    /**
     * 设置红黑树的根节点。
     *
     * @param root 新的根节点
     */
    protected void setRoot(Node root) {
        this.root = root;
    }

    /**
     * 判断节点 x 是否为红色；如果 x 为 null，则返回 false。
     *
     * @param x 要检查的节点
     * @return 如果节点为红色返回 true，否则返回 false；如果节点为 null 也返回 false
     */
    private boolean isRed(Node x) {
        // 如果节点为 null，则返回 false
        if (x == null) {
            return false;
        }
        // 检查节点的颜色是否为红色
        return x.color == RED;
    }

    /***************************************************************************
     *  节点判断程序方法。
     ***************************************************************************/

    /**
     * 返回以 x 为根节点的子树中的节点数量；如果 x 为 null，则返回 0。
     *
     * @param x 子树的根节点
     * @return 子树中的节点数量
     */
    private int size(Node x) {
        // 如果节点为 null，则返回 0
        if (x == null) {
            return 0;
        }
        // 返回节点的子树计数
        return x.size;
    }

    /**
     * 返回此符号表中的键值对数量。
     *
     * @return 此符号表中的键值对数量
     */
    public int size() {
        // 调用 size 方法，传入根节点
        return size(root);
    }

    /**
     * 判断此符号表是否为空。
     *
     * @return 如果此符号表为空，则返回 true；否则返回 false
     */
    public boolean isEmpty() {
        // 检查根节点是否为 null
        return root == null;
    }

    /***************************************************************************
     *  标准搜索。
     ***************************************************************************/

    /**
     * 返回与给定键关联的值。
     *
     * @param key 要查询的键
     * @return 如果键存在于符号表中，则返回与该键关联的值；否则返回 null
     * @throws IllegalArgumentException 如果给定的键为 null
     */
    public Value get(Key key) {
        // 检查键是否为 null
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        // 调用私有方法 get 来获取值
        return get(root, key);
    }

    /**
     * 递归查找与给定键关联的值；如果键不存在，则返回 null
     *
     * @param x   从哪个节点开始搜索
     * @param key 要查询的键
     * @return 如果找到键，则返回与之关联的值；否则返回 null
     */
    private Value get(Node x, Key key) {
        // 循环直到找到节点或者到达空节点
        while (x != null) {
            // 比较键与当前节点的键
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {  // 如果键小于当前节点的键，则移动到左子树
                x = x.left;
            } else if (cmp > 0) { // 如果键大于当前节点的键，则移动到右子树
                x = x.right;
            } else {
                return x.val; // 如果键等于当前节点的键，则返回该节点的值
            }
        }

        return null; // 如果没有找到键，则返回 null
    }


    /**
     * 判断此符号表是否包含给定的键。
     *
     * @param key 要检查的键
     * @return 如果此符号表包含给定的键，则返回 true；否则返回 false
     * @throws IllegalArgumentException 如果给定的键为 null
     */
    public boolean contains(Key key) {
        return get(key) != null; // 调用 get 方法，并检查返回值是否为 null
    }

    /***************************************************************************
     *  红黑树插入。
     ***************************************************************************/

    /**
     * 将指定的键值对插入到符号表中。如果符号表已经包含指定的键，则用新的值覆盖旧的值。
     * 如果指定的值为 null，则从符号表中删除指定的键（及其关联的值）。
     *
     * @param key 键
     * @param val 值
     * @throws IllegalArgumentException 如果键为 null
     */
    public void put(Key key, Value val) {
        // 检查键是否为 null
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }

        // 如果值为 null，则删除键
        if (val == null) {
            delete(key);
            return;
        }

        // 递归插入键值对
        root = put(root, key, val);
        // 将根节点的颜色设为黑色
        root.color = BLACK;
    }

    /**
     * 递归地在以 h 为根的子树中插入键值对
     *
     * @param h   当前节点
     * @param key 键
     * @param val 值
     * @return 插入后的节点
     */
    private Node put(Node h, Key key, Value val) {
        // 如果到达空节点，则创建一个新节点
        if (h == null) {
            return new Node(key, val, RED, 1);
        }

        // 比较键与当前节点的键
        int cmp = key.compareTo(h.key);
        if (cmp < 0) { // 如果键小于当前节点的键，则递归地在左子树中插入
            h.left = put(h.left, key, val);
        } else if (cmp > 0) {// 如果键大于当前节点的键，则递归地在右子树中插入
            h.right = put(h.right, key, val);
        } else {
            h.val = val;// 如果键等于当前节点的键，则更新该节点的值
        }

        // 维护红黑树的性质
        // 修复任何向右倾斜的链接

        // 如果右子节点为红色且左子节点不是红色，则进行左旋
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }

        // 如果左子节点为红色且左子节点的左子节点也为红色，则进行右旋
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }

        // 如果左右子节点均为红色，则翻转颜色
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }

        // 更新节点的子树计数
        h.size = size(h.left) + size(h.right) + 1;

        // 返回更新后的节点
        return h;
    }

    /***************************************************************************
     *  Red-black tree deletion.
     ***************************************************************************/

    /**
     * Removes the smallest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("BST underflow");
        }

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = deleteMin(root);
        if (!isEmpty()) {
            root.color = BLACK;
        }
        // assert check();
    }

    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node h) {
        if (h.left == null) {
            return null;
        }

        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }

        h.left = deleteMin(h.left);
        return balance(h);
    }

    /**
     * Removes the largest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("BST underflow");
        }

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = deleteMax(root);
        if (!isEmpty()) {
            root.color = BLACK;
        }
        // assert check();
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node h) {
        if (isRed(h.left)) {
            h = rotateRight(h);
        }

        if (h.right == null) {
            return null;
        }

        if (!isRed(h.right) && !isRed(h.right.left)) {
            h = moveRedRight(h);
        }

        h.right = deleteMax(h.right);

        return balance(h);
    }

    /**
     * 从符号表中删除指定的键及其关联的值（如果存在）。
     *
     * @param key 键
     * @throws IllegalArgumentException 如果键为 null
     */
    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        // 如果键不在符号表中，则直接返回
        if (!contains(key)) {
            return;
        }

        // 如果根节点的两个孩子都是黑色，则将根节点设为红色
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        // 递归删除键
        root = delete(root, key);
        // 如果树非空，则将根节点的颜色设为黑色
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    /**
     * 递归地在以 h 为根的子树中删除键
     *
     * @param h   当前节点
     * @param key 要删除的键
     * @return 删除后的节点
     */
    private Node delete(Node h, Key key) {
        // 如果键小于当前节点的键
        if (key.compareTo(h.key) < 0) {
            // 如果左子节点和左子节点的左子节点都不是红色
            if (!isRed(h.left) && !isRed(h.left.left)) {
                // 移动红色节点到左边
                h = moveRedLeft(h);
            }
            // 递归地在左子树中删除
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            if (key.compareTo(h.key) == 0 && (h.right == null)) {
                return null;
            }
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return balance(h);
    }

    /***************************************************************************
     *  Red-black tree helper functions.
     ***************************************************************************/

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // restore red-black tree invariant
    private Node balance(Node h) {
        // assert (h != null);

        if (isRed(h.right)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    /**
     * Returns the height of the BST (for debugging).
     *
     * @return the height of the BST (a 1-node tree has height 0)
     */
    public int height() {
        return height(root);
    }


    /***************************************************************************
     *  Utility functions.
     ***************************************************************************/

    private int height(Node x) {
        if (x == null) {
            return -1;
        }
        return 1 + Math.max(height(x.left), height(x.right));
    }

    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key min() {
        if (isEmpty()) {
            throw new NoSuchElementException("called min() with empty symbol table");
        }
        return min(root).key;
    }

    /***************************************************************************
     *  Ordered symbol table methods.
     ***************************************************************************/

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) {
        // assert x != null;
        if (x.left == null) {
            return x;
        } else {
            return min(x.left);
        }
    }

    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key max() {
        if (isEmpty()) {
            throw new NoSuchElementException("called max() with empty symbol table");
        }
        return max(root).key;
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) {
        // assert x != null;
        if (x.right == null) {
            return x;
        } else {
            return max(x.right);
        }
    }

    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     *
     * @param key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to floor() is null");
        }
        if (isEmpty()) {
            throw new NoSuchElementException("called floor() with empty symbol table");
        }
        Node x = floor(root, key);
        if (x == null) {
            return null;
        } else {
            return x.key;
        }
    }

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node floor(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x;
        }
        if (cmp < 0) {
            return floor(x.left, key);
        }
        Node t = floor(x.right, key);
        if (t != null) {
            return t;
        } else {
            return x;
        }
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     *
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to ceiling() is null");
        }
        if (isEmpty()) {
            throw new NoSuchElementException("called ceiling() with empty symbol table");
        }
        Node x = ceiling(root, key);
        if (x == null) {
            return null;
        } else {
            return x.key;
        }
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node ceiling(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x;
        }
        if (cmp > 0) {
            return ceiling(x.right, key);
        }
        Node t = ceiling(x.left, key);
        if (t != null) {
            return t;
        } else {
            return x;
        }
    }

    /**
     * Return the kth smallest key in the symbol table.
     *
     * @param k the order statistic
     * @return the {@code k}th smallest key in the symbol table
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *                                  <em>n</em>–1
     */
    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("called select() with invalid argument: " + k);
        }
        Node x = select(root, k);
        return x.key;
    }

    // the key of rank k in the subtree rooted at x
    private Node select(Node x, int k) {
        // assert x != null;
        // assert k >= 0 && k < size(x);
        int t = size(x.left);
        if (t > k) {
            return select(x.left, k);
        } else if (t < k) {
            return select(x.right, k - t - 1);
        } else {
            return x;
        }
    }

    /**
     * Return the number of keys in the symbol table strictly less than {@code key}.
     *
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to rank() is null");
        }
        return rank(key, root);
    }

    // number of keys less than key in the subtree rooted at x
    private int rank(Key key, Node x) {
        if (x == null) {
            return 0;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return rank(key, x.left);
        } else if (cmp > 0) {
            return 1 + size(x.left) + rank(key, x.right);
        } else {
            return size(x.left);
        }
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<Key> keys() {
        if (isEmpty()) {
            return new LinkedList<>();
        }
        return keys(min(), max());
    }

    /***************************************************************************
     *  Range count and range search.
     ***************************************************************************/

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return all keys in the sybol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive) as an {@code Iterable}
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) {
            throw new IllegalArgumentException("first argument to keys() is null");
        }
        if (hi == null) {
            throw new IllegalArgumentException("second argument to keys() is null");
        }

        Queue<Key> queue = new LinkedList<>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi);
        return queue;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) {
            return;
        }
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) {
            keys(x.left, queue, lo, hi);
        }
        if (cmplo <= 0 && cmphi >= 0) {
            queue.add(x.key);
        }
        if (cmphi > 0) {
            keys(x.right, queue, lo, hi);
        }
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in the sybol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int size(Key lo, Key hi) {
        if (lo == null) {
            throw new IllegalArgumentException("first argument to size() is null");
        }
        if (hi == null) {
            throw new IllegalArgumentException("second argument to size() is null");
        }

        if (lo.compareTo(hi) > 0) {
            return 0;
        }
        if (contains(hi)) {
            return rank(hi) - rank(lo) + 1;
        } else {
            return rank(hi) - rank(lo);
        }
    }

    /***************************************************************************
     *  Check integrity of red-black tree data structure.
     ***************************************************************************/
    public boolean check() {
//        if (!isBST())            StdOut.println("Not in symmetric order");
//        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
//        if (!isRankConsistent()) StdOut.println("Ranks not consistent");
//        if (!is23())             StdOut.println("Not a 2-3 tree");
//        if (!isBalanced())       StdOut.println("Not balanced");
        return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) {
            return true;
        }
        if (min != null && x.key.compareTo(min) <= 0) {
            return false;
        }
        if (max != null && x.key.compareTo(max) >= 0) {
            return false;
        }
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() {
        return isSizeConsistent(root);
    }

    private boolean isSizeConsistent(Node x) {
        if (x == null) {
            return true;
        }
        if (x.size != size(x.left) + size(x.right) + 1) {
            return false;
        }
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++) {
            if (i != rank(select(i))) {
                return false;
            }
        }
        for (Key key : keys()) {
            if (key.compareTo(select(rank(key))) != 0) {
                return false;
            }
        }
        return true;
    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean is23() {
        return is23(root);
    }

    private boolean is23(Node x) {
        if (x == null) {
            return true;
        }
        if (isRed(x.right)) {
            return false;
        }
        if (x != root && isRed(x) && isRed(x.left)) {
            return false;
        }
        return is23(x.left) && is23(x.right);
    }

    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() {
        int black = 0;     // number of black links on path from root to min
        Node x = root;
        while (x != null) {
            if (!isRed(x)) {
                black++;
            }
            x = x.left;
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node x, int black) {
        if (x == null) {
            return black == 0;
        }
        if (!isRed(x)) {
            black--;
        }
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    }

    // 节点数据类型
    protected class Node {
        private Key key;           // 键
        private Value val;         // 关联的数据
        private Node left, right;  // 指向左子树和右子树的链接
        private boolean color;     // 父链接的颜色
        private int size;          // 子树的节点计数

        // 构造函数
        // 初始化一个新的节点，指定键、值、颜色和子树计数
        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;        // 设置键
            this.val = val;        // 设置关联的数据
            this.color = color;    // 设置颜色
            this.size = size;      // 设置子树计数
        }

        // 重写 equals 方法
        // 比较两个 Node 对象是否相等
        @Override
        public boolean equals(Object o) {
            // 如果是同一个对象，则相等
            if (this == o) {
                return true;
            }
            // 如果是 null 或者类型不同，则不相等
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            // 强制转换为 Node 类型
            Node node = (Node) o;

            // 比较颜色是否相等
            if (color != node.color) {
                return false;
            }
            // 比较键是否相等
            if (!Objects.equals(key, node.key)) {
                return false;
            }
            // 比较值是否相等
            return Objects.equals(val, node.val);
        }

        // 重写 hashCode 方法
        // 返回 Node 对象的哈希码值
        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0; // 计算键的哈希码
            result = 31 * result + (val != null ? val.hashCode() : 0); // 计算值的哈希码
            result = 31 * result + (color ? 1 : 0); // 计算颜色的哈希码
            return result;
        }

        // 重写 toString 方法
        // 返回 Node 对象的字符串表示形式
        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key + // 输出键
                    ", val=" + val + // 输出关联的数据
//                ", left=" + left + // 输出左子树
//                ", right=" + right + // 输出右子树
                    ", color=" + color + // 输出颜色
                    ", size=" + size + // 输出子树计数
                    '}'; // 结束括号
        }

        // 深度相等方法
        // 深度比较两个 Node 对象是否相等
        public boolean deepEquals(Node o) {
            // 如果是同一个对象，则相等
            if (this == o) {
                return true;
            }
            // 如果是 null 或者类型不同，则不相等
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            // 先使用 equals 方法比较基本属性是否相等
            if (!this.equals(o)) {
                return false;
            }
            // 比较左子树是否深度相等
            if (this.left != null ? !this.left.deepEquals(o.left) : o.left != null) {
                return false;
            }
            // 比较右子树是否深度相等
            return this.right != null ? this.right.deepEquals(o.right) : o.right == null;
        }

        // Getter 和 Setter 方法
        // 提供对 Node 对象属性的访问和修改

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public Value getVal() {
            return val;
        }

        public void setVal(Value val) {
            this.val = val;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public boolean getColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }


}
