package misc

import (
	"reflect"
)

var (
	ReverseString func([]string)
)

func init() {
	MakeReverse(&ReverseString)
}

func reverse(in []reflect.Value) []reflect.Value {
	arr := in[0]
	if arr.Kind() == reflect.Slice {
		total := arr.Len()
		l := total - 1
		for i := 0; i < total/2; i++ {
			tmp := reflect.New(arr.Index(i).Type())
			tmp.Elem().Set(arr.Index(i))
			arr.Index(i).Set(arr.Index(l - i))
			arr.Index(l - i).Set(tmp.Elem())
		}
	}
	return []reflect.Value{}
}


func MakeFunc(fptr interface{}, fn func(args []reflect.Value) (results []reflect.Value)) {
	f := reflect.ValueOf(fptr).Elem()
	v := reflect.MakeFunc(f.Type(), fn)
	f.Set(v)
}


func MakeReverse(fptr interface{}) {
	MakeFunc(fptr, reverse)
}


func DropEmpty(in []string) []string {
	do := false
	for _, s := range in {
		if s == "" {
			do = true
			break
		}
	}
	if !do {
		return in
	}
	ret := make([]string, 0, len(in))
	for _, s := range in {
		if s != "" {
			ret = append(ret, s)
		}
	}
	return ret
}