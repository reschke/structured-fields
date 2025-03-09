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



## display-string


### basic display string (ascii content)

Input:
~~~
%"foo bar"
~~~

Result:
~~~
%"foo bar"
~~~

### all printable ascii

Input:
~~~
%" !%22#$%25&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
~~~

Result:
~~~
%" !%22#$%25&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
~~~

### non-ascii display string (uppercase escaping)

Input:
~~~
%"f%C3%BC%C3%BC"
~~~

Expects Parse Error
~~~
>>%"f%C3%BC%C3%BC"<<
  ----^ (0x43) Invalid percent escape sequence character '%' at position 4
~~~


### non-ascii display string (lowercase escaping)

Input:
~~~
%"f%c3%bc%c3%bc"
~~~

Result:
~~~
%"f%c3%bc%c3%bc"
~~~

### tab in display string

Input:
~~~
%"	"
~~~

Expects Parse Error
~~~
>>%"	"<<
  ---^ (0x22) Invalid character in Display String at position 3
~~~


### newline in display string

Input:
~~~
%"
"
~~~

Expects Parse Error
~~~
>>%"
"<<
  ---^ (0x22) Invalid character in Display String at position 3
~~~


### single quoted display string

Input:
~~~
%'foo'
~~~

Expects Parse Error
~~~
>>%'foo'<<
  --^ (0x66) DisplayString must continue with a double quote: 'foo''
~~~


### unquoted display string

Input:
~~~
%foo
~~~

Expects Parse Error
~~~
>>%foo<<
  --^ (0x6f) DisplayString must continue with a double quote: 'oo'
~~~


### display string missing initial quote

Input:
~~~
%foo"
~~~

Expects Parse Error
~~~
>>%foo"<<
  --^ (0x6f) DisplayString must continue with a double quote: 'oo"'
~~~


### unbalanced display string

Input:
~~~
%"foo
~~~

Expects Parse Error
~~~
>>%"foo<<
  -----^ Closing DQUOTE missing
~~~


### display string quoting

Input:
~~~
%"foo %22bar%22 \ baz"
~~~

Result:
~~~
%"foo %22bar%22 \ baz"
~~~

### bad display string escaping

Input:
~~~
%"foo %a
~~~

Expects Parse Error
~~~
>>%"foo %a<<
  --------^ Incomplete percent escape sequence at position 8
~~~


### bad display string utf-8 (invalid 2-byte seq)

Input:
~~~
%"%c3%28"
~~~

Expects Parse Error
~~~
>>%"%c3%28"<<
  --^ (0x25) Invalid UTF-8 sequence (Input length = 1) before position 2
~~~


### bad display string utf-8 (invalid sequence id)

Input:
~~~
%"%a0%a1"
~~~

Expects Parse Error
~~~
>>%"%a0%a1"<<
  --^ (0x25) Invalid UTF-8 sequence (Input length = 1) before position 2
~~~


### bad display string utf-8 (invalid hex)

Input:
~~~
%"%g0%1w"
~~~

Expects Parse Error
~~~
>>%"%g0%1w"<<
  ---^ (0x67) Invalid percent escape sequence character '%' at position 3
~~~


### bad display string utf-8 (invalid 3-byte seq)

Input:
~~~
%"%e2%28%a1"
~~~

Expects Parse Error
~~~
>>%"%e2%28%a1"<<
  --^ (0x25) Invalid UTF-8 sequence (Input length = 1) before position 2
~~~


### bad display string utf-8 (invalid 4-byte seq)

Input:
~~~
%"%f0%28%8c%28"
~~~

Expects Parse Error
~~~
>>%"%f0%28%8c%28"<<
  --^ (0x25) Invalid UTF-8 sequence (Input length = 1) before position 2
~~~


### BOM in display string

Input:
~~~
%"BOM: %ef%bb%bf"
~~~

Result:
~~~
%"BOM: %ef%bb%bf"
~~~


## examples


### Foo-Example

Input:
~~~
2; foourl="https://foo.example.com/"
~~~

Result:
~~~
2;foourl="https://foo.example.com/"
~~~

### Example-StrListHeader

Input:
~~~
"foo", "bar", "It was the best of times."
~~~

Result:
~~~
"foo", "bar", "It was the best of times."
~~~

### Example-Hdr (list on one line)

Input:
~~~
foo, bar
~~~

Result:
~~~
foo, bar
~~~

### Example-Hdr (list on two lines)

Input:
~~~
foo
bar
~~~

Result:
~~~
foo, bar
~~~

### Example-StrListListHeader

Input:
~~~
("foo" "bar"), ("baz"), ("bat" "one"), ()
~~~

Result:
~~~
("foo" "bar"), ("baz"), ("bat" "one"), ()
~~~

### Example-ListListParam

Input:
~~~
("foo"; a=1;b=2);lvl=5, ("bar" "baz");lvl=1
~~~

Result:
~~~
("foo";a=1;b=2);lvl=5, ("bar" "baz");lvl=1
~~~

### Example-ParamListHeader

Input:
~~~
abc;a=1;b=2; cde_456, (ghi;jk=4 l);q="9";r=w
~~~

Result:
~~~
abc;a=1;b=2;cde_456, (ghi;jk=4 l);q="9";r=w
~~~

### Example-IntHeader

Input:
~~~
1; a; b=?0
~~~

Result:
~~~
1;a;b=?0
~~~

### Example-DictHeader

Input:
~~~
en="Applepie", da=:w4ZibGV0w6ZydGU=:
~~~

Result:
~~~
en="Applepie", da=:w4ZibGV0w6ZydGU=:
~~~

### Example-DictHeader (boolean values)

Input:
~~~
a=?0, b, c; foo=bar
~~~

Result:
~~~
a=?0, b, c;foo=bar
~~~

### Example-DictListHeader

Input:
~~~
rating=1.5, feelings=(joy sadness)
~~~

Result:
~~~
rating=1.5, feelings=(joy sadness)
~~~

### Example-MixDict

Input:
~~~
a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid
~~~

Result:
~~~
a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid
~~~

### Example-Hdr (dictionary on one line)

Input:
~~~
foo=1, bar=2
~~~

Result:
~~~
foo=1, bar=2
~~~

### Example-Hdr (dictionary on two lines)

Input:
~~~
foo=1
bar=2
~~~

Result:
~~~
foo=1, bar=2
~~~

### Example-IntItemHeader

Input:
~~~
5
~~~

Result:
~~~
5
~~~

### Example-IntItemHeader (params)

Input:
~~~
5; foo=bar
~~~

Result:
~~~
5;foo=bar
~~~

### Example-IntegerHeader

Input:
~~~
42
~~~

Result:
~~~
42
~~~

### Example-FloatHeader

Input:
~~~
4.5
~~~

Result:
~~~
4.5
~~~

### Example-StringHeader

Input:
~~~
"hello world"
~~~

Result:
~~~
"hello world"
~~~

### Example-BinaryHdr

Input:
~~~
:cHJldGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==:
~~~

Result:
~~~
:cHJldGVuZCB0aGlzIGlzIGJpbmFyeSBjb250ZW50Lg==:
~~~

### Example-BoolHdr

Input:
~~~
?1
~~~

Result:
~~~
?1
~~~

