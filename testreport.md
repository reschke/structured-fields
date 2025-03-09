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


### spelled-out True boolean

Input:
~~~
?True
~~~

Expects Parse Error
~~~
>>?True<<
  -^ (0x54) Expected '0' or '1' in Boolean, found 'T'
~~~


### spelled-out False boolean

Input:
~~~
?False
~~~

Expects Parse Error
~~~
>>?False<<
  -^ (0x46) Expected '0' or '1' in Boolean, found 'F'
~~~



## date


### date - 1970-01-01 00:00:00

Input:
~~~
@0
~~~

Result:
~~~
@0
~~~

### date - 2022-08-04 01:57:13

Input:
~~~
@1659578233
~~~

Result:
~~~
@1659578233
~~~

### date - 1917-05-30 22:02:47

Input:
~~~
@-1659578233
~~~

Result:
~~~
@-1659578233
~~~

### date - 2^31

Input:
~~~
@2147483648
~~~

Result:
~~~
@2147483648
~~~

### date - 2^32

Input:
~~~
@4294967296
~~~

Result:
~~~
@4294967296
~~~

### date - decimal

Input:
~~~
@1659578233.12
~~~

Expects Parse Error
~~~
>>@1659578233.12<<
  -----------^ (0x2e) Extra characters in string parsed as Item
~~~



## dictionary


### basic dictionary

Input:
~~~
en="Applepie", da=:w4ZibGV0w6ZydGUK:
~~~

Result:
~~~
en="Applepie", da=:w4ZibGV0w6ZydGUK:
~~~

### empty dictionary

Input:
~~~

~~~

Result:
~~~

~~~

### single item dictionary

Input:
~~~
a=1
~~~

Result:
~~~
a=1
~~~

### list item dictionary

Input:
~~~
a=(1 2)
~~~

Result:
~~~
a=(1 2)
~~~

### single list item dictionary

Input:
~~~
a=(1)
~~~

Result:
~~~
a=(1)
~~~

### empty list item dictionary

Input:
~~~
a=()
~~~

Result:
~~~
a=()
~~~

### no whitespace dictionary

Input:
~~~
a=1,b=2
~~~

Result:
~~~
a=1, b=2
~~~

### extra whitespace dictionary

Input:
~~~
a=1 ,  b=2
~~~

Result:
~~~
a=1, b=2
~~~

### tab separated dictionary

Input:
~~~
a=1	,	b=2
~~~

Result:
~~~
a=1, b=2
~~~

### leading whitespace dictionary

Input:
~~~
     a=1 ,  b=2
~~~

Result:
~~~
a=1, b=2
~~~

### whitespace before = dictionary

Input:
~~~
a =1, b=2
~~~

Expects Parse Error
~~~
>>a =1, b=2<<
  --^ (0x3d) Expected COMMA in Dictionary, found: '=' (\u003d)
~~~


### whitespace after = dictionary

Input:
~~~
a=1, b= 2
~~~

Expects Parse Error
~~~
>>a=1, b= 2<<
  -------^ (0x20) Unexpected start character in Bare Item: ' ' (\u0020)
~~~


### two lines dictionary

Input:
~~~
a=1
b=2
~~~

Result:
~~~
a=1, b=2
~~~

### missing value dictionary

Input:
~~~
a=1, b, c=3
~~~

Result:
~~~
a=1, b, c=3
~~~

### all missing value dictionary

Input:
~~~
a, b, c
~~~

Result:
~~~
a, b, c
~~~

### start missing value dictionary

Input:
~~~
a, b=2
~~~

Result:
~~~
a, b=2
~~~

### end missing value dictionary

Input:
~~~
a=1, b
~~~

Result:
~~~
a=1, b
~~~

### missing value with params dictionary

Input:
~~~
a=1, b;foo=9, c=3
~~~

Result:
~~~
a=1, b;foo=9, c=3
~~~

### explicit true value with params dictionary

Input:
~~~
a=1, b=?1;foo=9, c=3
~~~

Result:
~~~
a=1, b;foo=9, c=3
~~~

### trailing comma dictionary

Input:
~~~
a=1, b=2,
~~~

Expects Parse Error
~~~
>>a=1, b=2,<<
  ---------^ Found trailing COMMA in Dictionary
~~~


### empty item dictionary

Input:
~~~
a=1,,b=2,
~~~

Expects Parse Error
~~~
>>a=1,,b=2,<<
  ----^ (0x2c) Key must start with LCALPHA or '*': ',' (\u002c)
~~~


### duplicate key dictionary

Input:
~~~
a=1,b=2,a=3
~~~

Result:
~~~
a=3, b=2
~~~

### numeric key dictionary

Input:
~~~
a=1,1b=2,a=1
~~~

Expects Parse Error
~~~
>>a=1,1b=2,a=1<<
  ----^ (0x31) Key must start with LCALPHA or '*': '1' (\u0031)
~~~


### uppercase key dictionary

Input:
~~~
a=1,B=2,a=1
~~~

Expects Parse Error
~~~
>>a=1,B=2,a=1<<
  ----^ (0x42) Key must start with LCALPHA or '*': 'B' (\u0042)
~~~


### bad key dictionary

Input:
~~~
a=1,b!=2,a=1
~~~

Expects Parse Error
~~~
>>a=1,b!=2,a=1<<
  -----^ (0x21) Expected COMMA in Dictionary, found: '!' (\u0021)
~~~





