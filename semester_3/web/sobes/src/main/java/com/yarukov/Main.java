package com.yarukov;

import java.util.*;

class Main{
    public static void main(String[] args) {
        // 1) Массивы
        int[] nums = new int[5];
        String strings[] = new String[3];
        nums[0] = 10;
        strings[0] = "Hello";
        Arrays.sort(nums);
        int nn = nums.length;
        System.out.println(nn);

        // 2) ArrayList
        ArrayList<Long> numbers = new ArrayList<>();
        numbers.add(10l);
        long a = numbers.get(0);
        numbers.set(0,20l);
        numbers.remove(0);
        Arrays.sort(numbers.toArray());
        int length = numbers.size();
        // 3) HashSet
        HashSet<Integer> set =  new HashSet<>();
        set.add(10);
        set.remove(10); // удаляет по значению
        boolean hasValue = set.contains(10); // проверка по значению
        // перебор
        for (int x : set){
            System.out.println(x);
        }
        // через итератор
        Iterator<Integer> it = set.iterator();
        while(it.hasNext()){
            int x = it.next();
        }
        // 4) TreeSet
        TreeSet<Integer> tree_set = new TreeSet<>();
        tree_set.add(10);
        int first = tree_set.first();
        int last = tree_set.last();
        tree_set.remove(first);
        // 5) HashMap
        HashMap<Integer,Integer> map = new HashMap<>();
        map.put(1,0);
        int val = map.get(1);
        boolean hasKey = map.containsKey(1);
        map.remove(1);

        for( Map.Entry<Integer,Integer> entry : map.entrySet()){
            int key = entry.getKey();
            int value = entry.getValue();
        }
        for( int key : map.keySet()){
            System.out.println(key + " = " + map.get(key));
        }
        // 6) ArrayDeque

        ArrayDeque<Integer> deq = new ArrayDeque<>();
        deq.addFirst(10);
        deq.addLast(10);
        int f = deq.getFirst();
        int l = deq.getLast();
        int n = deq.size();

        while(!deq.isEmpty()){
            deq.pollFirst();
            deq.pollLast();
        }
        // 7 LinkedList
        LinkedList<Integer> list = new LinkedList<>();
        list.addFirst(0);
        list.addLast(3);
        
        list.add(1, 15);   // вставить 15 на индекс 1
        int x = list.get(0);
        list.set(1, 25); // заменить элемент по индексу








    }

}