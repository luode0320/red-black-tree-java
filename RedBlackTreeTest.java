package com.pseudoyu.tree.rb;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RedBlackTreeTest {

    @Test
    public void countBlackRandom() {

        int num[] = randomCommon(0, 1000000, 10000);

        RedBlackTree<Integer, Integer> redBlackTree = new RedBlackTree<>();
        for (int i = 0; i < num.length; i++) {
            redBlackTree.put(num[i], num[i]);
        }

        RedBlackTree.Node root = redBlackTree.getRoot();

        for (int i = 0; i < 10000; i++) {
            int count = 1;
            RedBlackTree.Node node = root;
            while (1 == 1) {
                int flag= Math.random() > 0.5 ? 1 : 0;
                if (flag == 0) {
                    node = node.getRight();
                    System.out.print(1);
                } else {
                    node = node.getLeft();
                    System.out.print(0);
                }
                if (node == null) {
                    break;
                }
                if (!node.getColor()) {
                    count++;
                }
            }
            System.out.println();
            assertEquals(count, 11);
        }

    }

    @Test
    public void countBlackAll() {

        int num[] = randomCommon(0, 1000000, 10000);

        RedBlackTree<Integer, Integer> redBlackTree = new RedBlackTree<>();
        for (int i = 0; i < num.length; i++) {
            redBlackTree.put(num[i], num[i]);
        }

        RedBlackTree.Node root = redBlackTree.getRoot();
        countBlack(0, root);
    }

    public void countBlack(int cnt, RedBlackTree.Node node) {
        int count = cnt;
        if (node == null) {
            return;
        }
        if (!node.getColor()) {
            count++;
        }
        countBlack(count, node.getLeft());
        if (node.getLeft() == null) {
            assertEquals(count, 11);
        }
        countBlack(count, node.getRight());
        if (node.getRight() == null) {
            assertEquals(count, 11);
        }
        return;
    }

    public static int[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

}
