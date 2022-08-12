#include <iostream>
#include <functional>
#include <vector>
#include <any>

template<typename ...>
struct Curry;

template<typename Ret>
struct Curry<Ret> {
    std::function<Ret ()> function;
    explicit Curry(std::function<Ret ()> function): function(std::move(function)) {}
    operator Ret() {
        return function();
    }
};

template<typename Ret, typename First, typename ...Others>
struct Curry<Ret, First, Others...> {
    std::function<Ret (First, Others...)> function;
    explicit Curry(std::function<Ret (First, Others...)> function): function(std::move(function)) {}
    Curry<Ret, Others...> operator()(First arg) {
        return Curry<Ret, Others...>([function=function, arg=std::move(arg)](Others... others){
            return function(std::move(arg), others...);
        });
    }
    template<typename FirstArg, typename SecondArg, typename ...OtherArgs>
    auto operator()(FirstArg first, SecondArg second, OtherArgs... otherArgs) {
        return (*this)(first)(second, otherArgs...);
    }
};

template<typename Ret, typename ...Args>
Curry<Ret, Args...> makeCurry(std::function<Ret (Args...)> function) {
    return Curry<Ret, Args...>(std::move(function));
}

template<typename Ret>
Ret call(Curry<Ret> curryFunc, std::vector<std::any>::iterator unused) {
    return (Ret)(curryFunc);
}

template<typename Ret, typename First, typename ...Others>
Ret call(Curry<Ret, First, Others...> curryFunc, std::vector<std::any>::iterator args) {
    return call(curryFunc(std::any_cast<First>(*args)), args + 1);
}

int main() {
    std::function f = [](int a, int b, double c, int d) {return a + b + c + d;};
    std::cout << makeCurry(f)(3)(4)(5)(6) << std::endl;     // 18
    std::cout << makeCurry(f)(3, 4, 10.1)(6) << std::endl;  // 23.1
    std::vector<std::any> args = {1, 2, 3.4, 5};
    std::cout << call(makeCurry(f), args.begin()) << std::endl;  // 11.4
}