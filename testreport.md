# binary


## basic binary

Input:
~~~
:aGVsbG8=:
~~~

Result:
~~~
:aGVsbG8=:
~~~


## empty binary

Input:
~~~
::
~~~

Result:
~~~
::
~~~


## padding at beginning

Input:
~~~
:=aGVsbG8=:
~~~

Expects Parse Error
~~~
>>:=aGVsbG8=:<<
  -----------^ Input byte array has wrong 4-byte ending unit

~~~




