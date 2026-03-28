// https://leetcode.com/problems/regular-expression-matching/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class RegularExpressionMatching {
  public static void main(String[] args) {
    System.out.println(RegularExpretion.derivate("a", 'a').size());
    // System.out.println(isMatch("aaa", "ab*a*c*a"));
    System.out.println(isMatch("abcdede", "ab.*de"));
  }

  /*
   * public static boolean isMatch(String s, String p) {
   * StringBuilder pb = new StringBuilder(p);
   * for (int i = 0; i < pb.length(); i++) {
   * if (pb.charAt(i) == '*' && i < pb.length() - 1) {
   * if (pb.charAt(i - 1) == pb.charAt(i + 1)) {
   * pb.setCharAt(i, pb.charAt(i - 1));
   * pb.setCharAt(i + 1, '*');
   * }
   * }
   * }
   * int i = 0;
   * for (int j = 0; j < s.length();) {
   * if (i >= pb.length())
   * return false;
   * System.out.printf("i=%d, j=%d, p=%c, s=%c\n", i, j, pb.charAt(i),
   * s.charAt(j));
   * switch (pb.charAt(i)) {
   * case '*':
   * if (pb.charAt(i - 1) != s.charAt(j) && pb.charAt(i - 1) != '.') {
   * i++;
   * } else {
   * j++;
   * }
   * break;
   * default:
   * if (i < pb.length() - 1 && pb.charAt(i + 1) == '*') {
   * i++;
   * } else if (s.charAt(j) == pb.charAt(i) || pb.charAt(i) == '.') {
   * j++;
   * i++;
   * } else
   * return false;
   * }
   * }
   * return (i == pb.length());
   * }
   */ // OLD ;"
  public static boolean isPattrenContainEmpty(String s) {
    int i = 0;
    for (char c : s.toCharArray()) {
      if (c == '*')
        i++;
    }
    return i * 2 == s.length();
  }

  public static boolean isMatch(String s, String p) {
    HashMap<RegularExpretion, RegularExpretion> automate = new HashMap<>();
    RegularExpretion currentState;
    if (isPattrenContainEmpty(p)) {
      // System.out.println("YES");
      currentState = new RegularExpretion(new String[] { p, "" });

    } else
      currentState = new RegularExpretion(new String[] { p });
    Queue<RegularExpretion> queue = new LinkedList<>();
    queue.add(currentState);
    automate.put(currentState, currentState);
    while (!queue.isEmpty()) {
      RegularExpretion rg = queue.poll();
      System.out.println("DERIVATING: " + rg.toString());
      for (char c = 'a'; c <= 'z'; c++) {
        TreeSet<String> result = rg.derivate(c);
        if (result == null || result.isEmpty())
          continue;
        RegularExpretion newRg = new RegularExpretion(result);
        RegularExpretion prev = automate.get(newRg);

        if (prev == null) {
          automate.put(newRg, newRg);
          queue.add(newRg);
          prev = newRg;
        }

        rg.addTransition(prev, c);
      }

      System.out.println("insert: " + rg);
    }
    // for (RegularExpretion e : automate.values()) {
    // System.out.println(e);
    // }
    System.out.println("SIZE:" + automate.size());
    for (int i = 0; i < s.length() && currentState != null; i++) {
      System.out.println(currentState + "CODE:" + currentState.hashCode());
      currentState = currentState.transitionTo(s.charAt(i));
    }
    return (currentState != null) ? currentState.isFinalState() : false;
  }
}

class RegularExpretion {
  TreeSet<String> pattrens = null;
  private int code;
  Map<Character, RegularExpretion> transitions;
  boolean finalState;

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String s : this.pattrens) {
      sb.append(s + "\\/");
    }
    sb.append("\n");
    for (char key : this.transitions.keySet()) {
      sb.append("\t" + Character.toString(key) + "->" + this.transitions.get(key).pattrens + "ISFINAL="
          + Boolean.toString(this.transitions.get(key).finalState));
      sb.append("\n");
    }
    sb.append("\n");
    sb.append("isFinal=" + Boolean.toString(this.finalState));
    return sb.toString();
  }

  RegularExpretion(String[] pattrens) {
    this.transitions = new HashMap<>();
    this.pattrens = new TreeSet<String>(Arrays.asList(pattrens));
    this.finalState = this.pattrens.contains("");
    this.pattrens.remove("");
    setCode();
  }

  RegularExpretion(TreeSet<String> pattrens) {
    this.transitions = new HashMap<>();
    this.pattrens = pattrens;
    if (pattrens != null) {
      this.finalState = this.pattrens.contains("");
      this.pattrens.remove("");
      setCode();
    } else {
      this.code = -1;
    }

  }

  private void setCode() {
    if (pattrens == null)
      this.code = -1;
    else {
      StringBuilder sb = new StringBuilder();
      int i = 0;
      for (String s : this.pattrens) {
        sb.append(i);
        sb.append(s);
      }
      this.code = sb.toString().hashCode();
    }
    if (this.finalState)
      this.code++;
  }

  public boolean isFinalState() {
    return this.finalState;
  }

  public int hashCode() {
    return code;
  }

  public boolean equals(Object e) {
    return this.code == ((RegularExpretion) e).hashCode();
  }

  public void addTransition(RegularExpretion rg, char c) {
    this.transitions.put(c, rg);
  }

  public RegularExpretion transitionTo(char c) {
    RegularExpretion rg = this.transitions.get(c);
    return rg;
  }

  TreeSet<String> derivate(char c) {
    TreeSet<String> result = new TreeSet<>();
    for (String s : this.pattrens) {
      Set<String> tmp = derivate(s, c);
      if (tmp != null)
        result.addAll(tmp);
    }
    return result.size() != 0 ? result : null;
  }

  static Set<String> derivate(String s, char c) {
    if (s.length() == 0)
      return null;
    TreeSet<String> result = new TreeSet<>();
    StringBuilder sb = new StringBuilder();
    if (s.length() < 2 || s.charAt(1) != '*') {
      if (s.charAt(0) == c || s.charAt(0) == '.') {
        sb.append(s);
        sb.deleteCharAt(0);
        result.add(sb.toString());
        if (RegularExpressionMatching.isPattrenContainEmpty(sb.toString()))
          result.add("");
      }
    } else {
      sb.append(s);
      if (s.charAt(0) == c || s.charAt(0) == '.') {
        result.add(sb.toString());
        if (RegularExpressionMatching.isPattrenContainEmpty(sb.toString()))
          result.add("");
      }
      // System.out.println("SUBDIR:" + sb.toString() + " by" +
      // Character.toString(c));
      Set<String> secondResult = derivate(sb.delete(0, 2).toString(), c);
      if (secondResult != null)
        result.addAll(secondResult);
    }
    // System.out.println("Derivating " + s + " by " + Character.toString(c) + "
    // RESULT " + result);
    // RESULT " + result);
    return result.size() != 0 ? result : null;
  }

  List<Character> getDerivatableCharacters() {
    ArrayList<Character> result = new ArrayList<>();
    for (String s : this.pattrens) {
      boolean stop = false;
      for (int i = 0; i < s.length() && !stop; i++) {
        if (s.charAt(i) == '.') {
          for (char ch = 'a'; ch <= 'z'; ch++)
            result.add(ch);
        } else {

        }
      }
      if (result.size() == 'z' - 'a')
        break;
    }
    return result;
  }
}
