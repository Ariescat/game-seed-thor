package com.windforce.common.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * 随机数筛选器
 *
 * @author Kuang Hao
 * @since v1.0 2011-12-3
 */
public class SelectRandom<T> {
    /**
     * 筛选元素
     */
    private List<Element<T>> elements = new ArrayList<Element<T>>();
    /**
     * 游标
     */
    private int cusor = 0;

    public int size() {
        return elements.size();
    }

    /**
     * 添加元素
     *
     * @param value 筛选值
     * @param pro   选中概率权值
     * @return
     */
    public int addElement(T value, int pro) {
        Element<T> e = new Element<T>();
        e.setValue(value);
        e.setProButtom(cusor);
        e.setProTop(cusor + pro);
        this.cusor = this.cusor + pro;
        this.elements.add(e);
        return this.elements.size();
    }

    /**
     * 清除所有元素
     */
    public void clear() {
        this.elements.clear();
        this.cusor = 0;
    }

    /**
     * 筛选,线程安全
     *
     * @return
     */
    public T run() {

        long rLong = org.apache.commons.lang.math.RandomUtils.nextLong() % cusor;

        for (Element<T> element : this.elements) {
            if (element.getProTop() > rLong && element.getProButtom() <= rLong) {
                return element.getValue();
            }
        }
        return null;
    }

    /**
     * 筛选多个
     *
     * @param count
     * @return
     */
    public List<T> run(int count) {
        List<T> selects = new ArrayList<T>();
        if (count >= this.elements.size()) {
            for (Element<T> ele : this.elements) {
                selects.add(ele.getValue());
            }
            this.elements.clear();
            this.cusor = 0;
            return selects;
        } else {
            // SelectRandom<T> tempSelectRandom = this.clone();
            while (selects.size() < count) {
                T s = run();
                this.remove(s);
                // if (!selects.contains(s)) {
                selects.add(s);
                // }
            }
        }
        return selects;
    }

    /**
     * 随机筛选多个,如果超过最大数量，打乱赛选的顺序
     *
     * @param count
     * @return
     */
    public List<T> run(int count, boolean disorder) {
        List<T> selects = new ArrayList<T>();
        int sc = count;
        if (!disorder) {
            return this.run(count);
        }

        if (count > this.elements.size() && disorder) {
            sc = this.elements.size();
        }

        while (selects.size() < sc) {
            T s = run();
            this.remove(s);
            selects.add(s);
        }
        return selects;
    }

    @Override
    public SelectRandom<T> clone() {
        SelectRandom<T> selects = new SelectRandom<T>();
        for (Element<T> ele : this.elements) {
            selects.addElement(ele.getValue(), (ele.getProTop() - ele.getProButtom()));
        }
        return selects;
    }

    private void remove(T s) {
        Element<T> e = null;
        for (Element<T> ele : this.elements) {
            if (ele.getValue() == s) {
                e = ele;
                break;
            }
        }
        if (e == null) {
            System.out.println();
        }
        int range = (e.getProTop() - e.getProButtom());
        boolean flag = false;
        for (Element<T> ele : this.elements) {
            if (flag) {
                ele.setProButtom(ele.getProButtom() - range);
                ele.setProTop(ele.getProTop() - range);
            }
            if (ele == e) {
                flag = true;
            }
        }
        this.cusor -= range;
        this.elements.remove(e);
    }
}

class Element<T> {
    /**
     * 值对象
     */
    private T value;
    /**
     * 上限
     */
    private int proTop;
    /**
     * 下限
     */
    private int proButtom;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getProTop() {
        return proTop;
    }

    public void setProTop(int proTop) {
        this.proTop = proTop;
    }

    public int getProButtom() {
        return proButtom;
    }

    public void setProButtom(int proButtom) {
        this.proButtom = proButtom;
    }
}
