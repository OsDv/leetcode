mod solution;
fn main() {
    println!("Hello, world!");
    let result = solution::Solution::four_sum(vec![1000000000,1000000000,1000000000,1000000000,1000000000],0);
    for v in result {
        println!("[{},{},{},{}]",v[0],v[1],v[2],v[3]);
    }
}
