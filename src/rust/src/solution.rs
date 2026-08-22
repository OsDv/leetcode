pub struct Solution ();
#[derive(PartialEq, Eq, Clone, Debug)]
pub struct ListNode {
  pub val: i32,
  pub next: Option<Box<ListNode>>
}
#[derive(Debug)]
struct ListNodeStaticBTElement{
    val: Option<Box<ListNode>>,
    right: i64,
    left:i64, 
}
struct ListNodeStaticBT {
    tree:Vec<ListNodeStaticBTElement>,
    root: i64,
    free: i64
}
impl ListNodeStaticBT {
    fn new(capacity:usize) -> Self {
        let mut result = ListNodeStaticBT {
            tree: Vec::with_capacity(capacity),
            free:0,
            root: -1
        };
        for i in 0..capacity {result.tree.push(ListNodeStaticBTElement{val:None,left:i as i64+1,right:-1})};
        if let Some(val) = result.tree.last_mut() {val.left = -1}
        return result;
    }
    fn insert(&mut self,element: ListNodeStaticBTElement) -> bool {
        if self.free == -1 {return false}; // tree is full can't insert
        // insert in the free spot
        let loc = self.free;
        self.free = self.tree[self.free as usize].left;
        let mut parent = self.root;
        if parent == -1 { // empty tree
            self.root = loc;
            self.tree[loc as usize] = element;
            return true;
        }

        loop { // BST insertion with duplicate
            if self.tree[parent as usize].val.as_ref().unwrap().val < element.val.as_ref().unwrap().val{
                if self.tree[parent as usize].right == -1 {
                    self.tree[parent as usize].right = loc;
                    break;
                } else {parent = self.tree[parent as usize].right}
            } else {
                if self.tree[parent as usize].left == -1 {
                    self.tree[parent as usize].left = loc;
                    break;
                } else {parent = self.tree[parent as usize].left}
            }
        }
        self.tree[loc as usize] = element;
        true
    }
    fn min_index(&self) -> (i64,i64) {
        if self.root == -1 {return (-1,-1)};
        let mut p = self.root;
        let mut parent = -1;
        loop {
            if self.tree[p as usize].left == -1 {return (parent,p)}
            parent = p;
            p = self.tree[p as usize].left;
        }
    }
    fn remove_left(&mut self, parent:i64){
        if self.root == -1 {return}
        if parent == -1 { // remove root
            if self.tree[self.root as usize].left > 0 {return}
            let root = self.root;
            self.tree[root as usize].left = self.free;
            self.root = self.tree[root as usize].right;
            self.free = root;
            return;
        }
        let child = self.tree[parent as usize].left;
        self.tree[parent as usize].left = self.tree[child as usize].right;
        self.tree[child as usize].left = self.free;
        self.free = child;
    }
}
impl ListNode {
  #[inline]
  fn new(val: i32) -> Self {
    ListNode {
      next: None,
      val
    }
  }
}
impl Solution {
    pub fn four_sum(nums: Vec<i32>, target: i32) -> Vec<Vec<i32>> {
        // if the vector doesn't contain at leas 4 elements return empty vector
        if nums.len() < 4  {return Vec::new()};

        let mut nums = nums.clone();
        nums.sort();
        let mut result:  Vec<Vec<i32>> = Vec::new();
        let mut max = i32::MAX;
        let mut min = i32::MIN;
        if let Some(val) = nums[0].checked_add(nums[1]) {
            if let Some(val) = val.checked_add(nums[2]){
                max = target - val;
            }
        }
        if let Some(val) = nums[nums.len()-1].checked_add(nums[nums.len()-2]) {
            if let Some(val) = val.checked_add(nums[nums.len()-3]){
                min = target - val;
            }
        }
        let mut i =0;
        while i<nums.len() && nums[i] < min {i+=1};
        if i == nums.len() {return Vec::new()};
        let mut old_i= nums[i]+1;
        while i<nums.len()-3 && nums[i] <= max {
            if nums[i] == old_i {i+=1;continue};
            let mut j = i+1;
            let mut old_j = nums[j]+1;
            while j<nums.len()-2 && nums[j] <= max {
                if nums[j] == old_j {j+=1;continue};
                let mut k=j+1;
                let mut old_k = nums[k] +1;
                while k<nums.len()-1 && nums[k] <= max {
                    if nums[k] == old_k  {k+=1;continue};
                    //println!("DEBUG: i.{}, j.{}, k.{}",nums[i],nums[j],nums[k]);
                    if let Some(sum) = nums[i].checked_add(nums[j]){
                        if let Some(sum) = sum.checked_add(nums[k]){
                            let search_for = target - sum;
                            let l_range = &nums[k+1..];
                            if let Result::Ok(l) = l_range.binary_search(&search_for) {
                                result.push(vec![nums[i],nums[j],nums[k],nums[l+k+1]]);
                            };
                        }
                    } 
                    old_k = nums[k];
                    k+=1;
                }
                old_j = nums[j];
                j+=1;
            }
            old_i = nums[i];
            i+=1;
        }

        return result;
    }
    //https://leetcode.com/problems/merge-k-sorted-lists
    pub fn merge_k_lists(lists: Vec<Option<Box<ListNode>>>) -> Option<Box<ListNode>> {
        if lists.len() == 0 {return None}
        let mut tree = ListNodeStaticBT::new(lists.len());
        for list in lists {
            if let Some(val) = list {
                let element = ListNodeStaticBTElement {val:Some(val),right:-1,left:-1};
                tree.insert(element);
            }
        }
        let mut result : Option<Box<ListNode>> = None;
        let mut tail  = &mut result;
        while tree.root != -1 {
            let (parent,min)= tree.min_index();
            let mut node = tree.tree[min as usize].val.take().unwrap();
            tree.remove_left(parent);
            let next = node.next.take();
            if let Some(val)= next {tree.insert(ListNodeStaticBTElement{ val:Some(val),left:-1,right:-1});}
            *tail = Some(node);
            tail = &mut tail.as_mut().unwrap().next;
        };
        return result;
    }
}
