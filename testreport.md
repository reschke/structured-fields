# Test Report


## binary


### basic binary

Input:
~~~
:aGVsbG8=:
~~~

Result:
~~~
:aGVsbG8=:
~~~

### empty binary

Input:
~~~
::
~~~

Result:
~~~
::
~~~

### padding at beginning

Input:
~~~
:=aGVsbG8=:
~~~

Expects Parse Error
~~~
>>:=aGVsbG8=:<<
  -----------^ Input byte array has wrong 4-byte ending unit
~~~


### padding in middle

Input:
~~~
:a=GVsbG8=:
~~~

Expects Parse Error
~~~
>>:a=GVsbG8=:<<
  -----------^ Last unit does not have enough valid bits
~~~


### bad padding

Input:
~~~
:aGVsbG8:
~~~

Result:
~~~
:aGVsbG8=:
~~~

### bad padding dot

Input:
~~~
:aGVsbG8.:
~~~

Expects Parse Error
~~~
>>:aGVsbG8.:<<
  ---------^ (0x3a) Invalid Byte Sequence Character '.' at position 9
~~~


### bad end delimiter

Input:
~~~
:aGVsbG8=
~~~

Expects Parse Error
~~~
>>:aGVsbG8=<<
  ---------^ Byte Sequence must end with COLON: 'aGVsbG8='
~~~


### extra whitespace

Input:
~~~
:aGVsb G8=:
~~~

Expects Parse Error
~~~
>>:aGVsb G8=:<<
  -------^ (0x47) Invalid Byte Sequence Character ' ' at position 7
~~~


### all whitespace

Input:
~~~
:    :
~~~

Expects Parse Error
~~~
>>:    :<<
  --^ (0x20) Invalid Byte Sequence Character ' ' at position 2
~~~


### extra chars

Input:
~~~
:aGVsbG!8=:
~~~

Expects Parse Error
~~~
>>:aGVsbG!8=:<<
  --------^ (0x38) Invalid Byte Sequence Character '!' at position 8
~~~


### suffix chars

Input:
~~~
:aGVsbG8=!:
~~~

Expects Parse Error
~~~
>>:aGVsbG8=!:<<
  ----------^ (0x3a) Invalid Byte Sequence Character '!' at position 10
~~~


### non-zero pad bits

Input:
~~~
:iZ==:
~~~

Result:
~~~
:iQ==:
~~~

### non-ASCII binary

Input:
~~~
:/+Ah:
~~~

Result:
~~~
:/+Ah:
~~~

### base64url binary

Input:
~~~
:_-Ah:
~~~

Expects Parse Error
~~~
>>:_-Ah:<<
  --^ (0x2d) Invalid Byte Sequence Character '_' at position 2
~~~



## boolean


### basic true boolean

Input:
~~~
?1
~~~

Result:
~~~
?1
~~~

### basic false boolean

Input:
~~~
?0
~~~

Result:
~~~
?0
~~~

### unknown boolean

Input:
~~~
?Q
~~~

Expects Parse Error
~~~
>>?Q<<
  -^ (0x51) Expected '0' or '1' in Boolean, found 'Q'
~~~


### whitespace boolean

Input:
~~~
? 1
~~~

Expects Parse Error
~~~
>>? 1<<
  -^ (0x20) Expected '0' or '1' in Boolean, found ' '
~~~


### negative zero boolean

Input:
~~~
?-0
~~~

Expects Parse Error
~~~
>>?-0<<
  -^ (0x2d) Expected '0' or '1' in Boolean, found '-'
~~~


### T boolean

Input:
~~~
?T
~~~

Expects Parse Error
~~~
>>?T<<
  -^ (0x54) Expected '0' or '1' in Boolean, found 'T'
~~~


### F boolean

Input:
~~~
?F
~~~

Expects Parse Error
~~~
>>?F<<
  -^ (0x46) Expected '0' or '1' in Boolean, found 'F'
~~~


### t boolean

Input:
~~~
?t
~~~

Expects Parse Error
~~~
>>?t<<
  -^ (0x74) Expected '0' or '1' in Boolean, found 't'
~~~


### f boolean

Input:
~~~
?f
~~~

Expects Parse Error
~~~
>>?f<<
  -^ (0x66) Expected '0' or '1' in Boolean, found 'f'
~~~
