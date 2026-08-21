pub struct Solution ();
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
}
