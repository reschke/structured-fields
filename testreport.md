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


## item


### empty item

Input:
~~~

~~~

Expects Parse Error
~~~
>><<
  ^ Empty string found when parsing Bare Item
~~~


### leading space

Input:
~~~
 	 1
~~~

Expects Parse Error
~~~
>> 	 1<<
  -^ (0x09) Unexpected start character in Bare Item: HTAB (\u0009)
~~~


### trailing space

Input:
~~~
1 	 
~~~

Expects Parse Error
~~~
>>1 	 <<
  --^ (0x09) Extra characters in string parsed as Item
~~~


### leading and trailing space

Input:
~~~
  1  
~~~

Result:
~~~
1
~~~

### leading and trailing whitespace

Input:
~~~
     1  
~~~

Result:
~~~
1
~~~


## key-generated


### 0x00 as a single-character dictionary key

Input:
~~~
 =1
~~~

Expects Parse Error
~~~
>> =1<<
  ^ (0x00) Key must start with LCALPHA or '*': ' ' (\u0000)
~~~


### 0x01 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x01) Key must start with LCALPHA or '*': '' (\u0001)
~~~


### 0x02 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x02) Key must start with LCALPHA or '*': '' (\u0002)
~~~


### 0x03 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x03) Key must start with LCALPHA or '*': '' (\u0003)
~~~


### 0x04 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x04) Key must start with LCALPHA or '*': '' (\u0004)
~~~


### 0x05 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x05) Key must start with LCALPHA or '*': '' (\u0005)
~~~


### 0x06 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x06) Key must start with LCALPHA or '*': '' (\u0006)
~~~


### 0x07 as a single-character dictionary key

Input:
~~~
 =1
~~~

Expects Parse Error
~~~
>> =1<<
  ^ (0x07) Key must start with LCALPHA or '*': ' ' (\u0007)
~~~


### 0x08 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x08) Key must start with LCALPHA or '*': '' (\u0008)
~~~


### 0x09 as a single-character dictionary key

Input:
~~~
	=1
~~~

Expects Parse Error
~~~
>>	=1<<
  ^ (0x09) Key must start with LCALPHA or '*': HTAB (\u0009)
~~~


### 0x0a as a single-character dictionary key

Input:
~~~

=1
~~~

Expects Parse Error
~~~
>>
=1<<
  ^ (0x0a) Key must start with LCALPHA or '*': '
' (\u000a)
~~~


### 0x0b as a single-character dictionary key

Input:
~~~

=1
~~~

Expects Parse Error
~~~
>>
=1<<
  ^ (0x0b) Key must start with LCALPHA or '*': '
' (\u000b)
~~~


### 0x0c as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x0c) Key must start with LCALPHA or '*': '' (\u000c)
~~~


### 0x0d as a single-character dictionary key

Input:
~~~

=1
~~~

Expects Parse Error
~~~
>>
=1<<
  ^ (0x0d) Key must start with LCALPHA or '*': '
' (\u000d)
~~~


### 0x0e as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x0e) Key must start with LCALPHA or '*': '' (\u000e)
~~~


### 0x0f as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x0f) Key must start with LCALPHA or '*': '' (\u000f)
~~~


### 0x10 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x10) Key must start with LCALPHA or '*': '' (\u0010)
~~~


### 0x11 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x11) Key must start with LCALPHA or '*': '' (\u0011)
~~~


### 0x12 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x12) Key must start with LCALPHA or '*': '' (\u0012)
~~~


### 0x13 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x13) Key must start with LCALPHA or '*': '' (\u0013)
~~~


### 0x14 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x14) Key must start with LCALPHA or '*': '' (\u0014)
~~~


### 0x15 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x15) Key must start with LCALPHA or '*': '' (\u0015)
~~~


### 0x16 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x16) Key must start with LCALPHA or '*': '' (\u0016)
~~~


### 0x17 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x17) Key must start with LCALPHA or '*': '' (\u0017)
~~~


### 0x18 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x18) Key must start with LCALPHA or '*': '' (\u0018)
~~~


### 0x19 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x19) Key must start with LCALPHA or '*': '' (\u0019)
~~~


### 0x1a as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x1a) Key must start with LCALPHA or '*': '' (\u001a)
~~~


### 0x1b as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x1b) Key must start with LCALPHA or '*': '' (\u001b)
~~~


### 0x1c as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x1c) Key must start with LCALPHA or '*': '' (\u001c)
~~~


### 0x1d as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x1d) Key must start with LCALPHA or '*': '' (\u001d)
~~~


### 0x1e as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x1e) Key must start with LCALPHA or '*': '' (\u001e)
~~~


### 0x1f as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x1f) Key must start with LCALPHA or '*': '' (\u001f)
~~~


### 0x20 as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x3d) Key must start with LCALPHA or '*': '=' (\u003d)
~~~


### 0x21 as a single-character dictionary key

Input:
~~~
!=1
~~~

Expects Parse Error
~~~
>>!=1<<
  ^ (0x21) Key must start with LCALPHA or '*': '!' (\u0021)
~~~


### 0x22 as a single-character dictionary key

Input:
~~~
"=1
~~~

Expects Parse Error
~~~
>>"=1<<
  ^ (0x22) Key must start with LCALPHA or '*': '"' (\u0022)
~~~


### 0x23 as a single-character dictionary key

Input:
~~~
#=1
~~~

Expects Parse Error
~~~
>>#=1<<
  ^ (0x23) Key must start with LCALPHA or '*': '#' (\u0023)
~~~


### 0x24 as a single-character dictionary key

Input:
~~~
$=1
~~~

Expects Parse Error
~~~
>>$=1<<
  ^ (0x24) Key must start with LCALPHA or '*': '$' (\u0024)
~~~


### 0x25 as a single-character dictionary key

Input:
~~~
%=1
~~~

Expects Parse Error
~~~
>>%=1<<
  ^ (0x25) Key must start with LCALPHA or '*': '%' (\u0025)
~~~


### 0x26 as a single-character dictionary key

Input:
~~~
&=1
~~~

Expects Parse Error
~~~
>>&=1<<
  ^ (0x26) Key must start with LCALPHA or '*': '&' (\u0026)
~~~


### 0x27 as a single-character dictionary key

Input:
~~~
'=1
~~~

Expects Parse Error
~~~
>>'=1<<
  ^ (0x27) Key must start with LCALPHA or '*': ''' (\u0027)
~~~


### 0x28 as a single-character dictionary key

Input:
~~~
(=1
~~~

Expects Parse Error
~~~
>>(=1<<
  ^ (0x28) Key must start with LCALPHA or '*': '(' (\u0028)
~~~


### 0x29 as a single-character dictionary key

Input:
~~~
)=1
~~~

Expects Parse Error
~~~
>>)=1<<
  ^ (0x29) Key must start with LCALPHA or '*': ')' (\u0029)
~~~


### 0x2a as a single-character dictionary key

Input:
~~~
*=1
~~~

Result:
~~~
*=1
~~~

### 0x2b as a single-character dictionary key

Input:
~~~
+=1
~~~

Expects Parse Error
~~~
>>+=1<<
  ^ (0x2b) Key must start with LCALPHA or '*': '+' (\u002b)
~~~


### 0x2c as a single-character dictionary key

Input:
~~~
,=1
~~~

Expects Parse Error
~~~
>>,=1<<
  ^ (0x2c) Key must start with LCALPHA or '*': ',' (\u002c)
~~~


### 0x2d as a single-character dictionary key

Input:
~~~
-=1
~~~

Expects Parse Error
~~~
>>-=1<<
  ^ (0x2d) Key must start with LCALPHA or '*': '-' (\u002d)
~~~


### 0x2e as a single-character dictionary key

Input:
~~~
.=1
~~~

Expects Parse Error
~~~
>>.=1<<
  ^ (0x2e) Key must start with LCALPHA or '*': '.' (\u002e)
~~~


### 0x2f as a single-character dictionary key

Input:
~~~
/=1
~~~

Expects Parse Error
~~~
>>/=1<<
  ^ (0x2f) Key must start with LCALPHA or '*': '/' (\u002f)
~~~


### 0x30 as a single-character dictionary key

Input:
~~~
0=1
~~~

Expects Parse Error
~~~
>>0=1<<
  ^ (0x30) Key must start with LCALPHA or '*': '0' (\u0030)
~~~


### 0x31 as a single-character dictionary key

Input:
~~~
1=1
~~~

Expects Parse Error
~~~
>>1=1<<
  ^ (0x31) Key must start with LCALPHA or '*': '1' (\u0031)
~~~


### 0x32 as a single-character dictionary key

Input:
~~~
2=1
~~~

Expects Parse Error
~~~
>>2=1<<
  ^ (0x32) Key must start with LCALPHA or '*': '2' (\u0032)
~~~


### 0x33 as a single-character dictionary key

Input:
~~~
3=1
~~~

Expects Parse Error
~~~
>>3=1<<
  ^ (0x33) Key must start with LCALPHA or '*': '3' (\u0033)
~~~


### 0x34 as a single-character dictionary key

Input:
~~~
4=1
~~~

Expects Parse Error
~~~
>>4=1<<
  ^ (0x34) Key must start with LCALPHA or '*': '4' (\u0034)
~~~


### 0x35 as a single-character dictionary key

Input:
~~~
5=1
~~~

Expects Parse Error
~~~
>>5=1<<
  ^ (0x35) Key must start with LCALPHA or '*': '5' (\u0035)
~~~


### 0x36 as a single-character dictionary key

Input:
~~~
6=1
~~~

Expects Parse Error
~~~
>>6=1<<
  ^ (0x36) Key must start with LCALPHA or '*': '6' (\u0036)
~~~


### 0x37 as a single-character dictionary key

Input:
~~~
7=1
~~~

Expects Parse Error
~~~
>>7=1<<
  ^ (0x37) Key must start with LCALPHA or '*': '7' (\u0037)
~~~


### 0x38 as a single-character dictionary key

Input:
~~~
8=1
~~~

Expects Parse Error
~~~
>>8=1<<
  ^ (0x38) Key must start with LCALPHA or '*': '8' (\u0038)
~~~


### 0x39 as a single-character dictionary key

Input:
~~~
9=1
~~~

Expects Parse Error
~~~
>>9=1<<
  ^ (0x39) Key must start with LCALPHA or '*': '9' (\u0039)
~~~


### 0x3a as a single-character dictionary key

Input:
~~~
:=1
~~~

Expects Parse Error
~~~
>>:=1<<
  ^ (0x3a) Key must start with LCALPHA or '*': ':' (\u003a)
~~~


### 0x3b as a single-character dictionary key

Input:
~~~
;=1
~~~

Expects Parse Error
~~~
>>;=1<<
  ^ (0x3b) Key must start with LCALPHA or '*': ';' (\u003b)
~~~


### 0x3c as a single-character dictionary key

Input:
~~~
<=1
~~~

Expects Parse Error
~~~
>><=1<<
  ^ (0x3c) Key must start with LCALPHA or '*': '<' (\u003c)
~~~


### 0x3d as a single-character dictionary key

Input:
~~~
==1
~~~

Expects Parse Error
~~~
>>==1<<
  ^ (0x3d) Key must start with LCALPHA or '*': '=' (\u003d)
~~~


### 0x3e as a single-character dictionary key

Input:
~~~
>=1
~~~

Expects Parse Error
~~~
>>>=1<<
  ^ (0x3e) Key must start with LCALPHA or '*': '>' (\u003e)
~~~


### 0x3f as a single-character dictionary key

Input:
~~~
?=1
~~~

Expects Parse Error
~~~
>>?=1<<
  ^ (0x3f) Key must start with LCALPHA or '*': '?' (\u003f)
~~~


### 0x40 as a single-character dictionary key

Input:
~~~
@=1
~~~

Expects Parse Error
~~~
>>@=1<<
  ^ (0x40) Key must start with LCALPHA or '*': '@' (\u0040)
~~~


### 0x41 as a single-character dictionary key

Input:
~~~
A=1
~~~

Expects Parse Error
~~~
>>A=1<<
  ^ (0x41) Key must start with LCALPHA or '*': 'A' (\u0041)
~~~


### 0x42 as a single-character dictionary key

Input:
~~~
B=1
~~~

Expects Parse Error
~~~
>>B=1<<
  ^ (0x42) Key must start with LCALPHA or '*': 'B' (\u0042)
~~~


### 0x43 as a single-character dictionary key

Input:
~~~
C=1
~~~

Expects Parse Error
~~~
>>C=1<<
  ^ (0x43) Key must start with LCALPHA or '*': 'C' (\u0043)
~~~


### 0x44 as a single-character dictionary key

Input:
~~~
D=1
~~~

Expects Parse Error
~~~
>>D=1<<
  ^ (0x44) Key must start with LCALPHA or '*': 'D' (\u0044)
~~~


### 0x45 as a single-character dictionary key

Input:
~~~
E=1
~~~

Expects Parse Error
~~~
>>E=1<<
  ^ (0x45) Key must start with LCALPHA or '*': 'E' (\u0045)
~~~


### 0x46 as a single-character dictionary key

Input:
~~~
F=1
~~~

Expects Parse Error
~~~
>>F=1<<
  ^ (0x46) Key must start with LCALPHA or '*': 'F' (\u0046)
~~~


### 0x47 as a single-character dictionary key

Input:
~~~
G=1
~~~

Expects Parse Error
~~~
>>G=1<<
  ^ (0x47) Key must start with LCALPHA or '*': 'G' (\u0047)
~~~


### 0x48 as a single-character dictionary key

Input:
~~~
H=1
~~~

Expects Parse Error
~~~
>>H=1<<
  ^ (0x48) Key must start with LCALPHA or '*': 'H' (\u0048)
~~~


### 0x49 as a single-character dictionary key

Input:
~~~
I=1
~~~

Expects Parse Error
~~~
>>I=1<<
  ^ (0x49) Key must start with LCALPHA or '*': 'I' (\u0049)
~~~


### 0x4a as a single-character dictionary key

Input:
~~~
J=1
~~~

Expects Parse Error
~~~
>>J=1<<
  ^ (0x4a) Key must start with LCALPHA or '*': 'J' (\u004a)
~~~


### 0x4b as a single-character dictionary key

Input:
~~~
K=1
~~~

Expects Parse Error
~~~
>>K=1<<
  ^ (0x4b) Key must start with LCALPHA or '*': 'K' (\u004b)
~~~


### 0x4c as a single-character dictionary key

Input:
~~~
L=1
~~~

Expects Parse Error
~~~
>>L=1<<
  ^ (0x4c) Key must start with LCALPHA or '*': 'L' (\u004c)
~~~


### 0x4d as a single-character dictionary key

Input:
~~~
M=1
~~~

Expects Parse Error
~~~
>>M=1<<
  ^ (0x4d) Key must start with LCALPHA or '*': 'M' (\u004d)
~~~


### 0x4e as a single-character dictionary key

Input:
~~~
N=1
~~~

Expects Parse Error
~~~
>>N=1<<
  ^ (0x4e) Key must start with LCALPHA or '*': 'N' (\u004e)
~~~


### 0x4f as a single-character dictionary key

Input:
~~~
O=1
~~~

Expects Parse Error
~~~
>>O=1<<
  ^ (0x4f) Key must start with LCALPHA or '*': 'O' (\u004f)
~~~


### 0x50 as a single-character dictionary key

Input:
~~~
P=1
~~~

Expects Parse Error
~~~
>>P=1<<
  ^ (0x50) Key must start with LCALPHA or '*': 'P' (\u0050)
~~~


### 0x51 as a single-character dictionary key

Input:
~~~
Q=1
~~~

Expects Parse Error
~~~
>>Q=1<<
  ^ (0x51) Key must start with LCALPHA or '*': 'Q' (\u0051)
~~~


### 0x52 as a single-character dictionary key

Input:
~~~
R=1
~~~

Expects Parse Error
~~~
>>R=1<<
  ^ (0x52) Key must start with LCALPHA or '*': 'R' (\u0052)
~~~


### 0x53 as a single-character dictionary key

Input:
~~~
S=1
~~~

Expects Parse Error
~~~
>>S=1<<
  ^ (0x53) Key must start with LCALPHA or '*': 'S' (\u0053)
~~~


### 0x54 as a single-character dictionary key

Input:
~~~
T=1
~~~

Expects Parse Error
~~~
>>T=1<<
  ^ (0x54) Key must start with LCALPHA or '*': 'T' (\u0054)
~~~


### 0x55 as a single-character dictionary key

Input:
~~~
U=1
~~~

Expects Parse Error
~~~
>>U=1<<
  ^ (0x55) Key must start with LCALPHA or '*': 'U' (\u0055)
~~~


### 0x56 as a single-character dictionary key

Input:
~~~
V=1
~~~

Expects Parse Error
~~~
>>V=1<<
  ^ (0x56) Key must start with LCALPHA or '*': 'V' (\u0056)
~~~


### 0x57 as a single-character dictionary key

Input:
~~~
W=1
~~~

Expects Parse Error
~~~
>>W=1<<
  ^ (0x57) Key must start with LCALPHA or '*': 'W' (\u0057)
~~~


### 0x58 as a single-character dictionary key

Input:
~~~
X=1
~~~

Expects Parse Error
~~~
>>X=1<<
  ^ (0x58) Key must start with LCALPHA or '*': 'X' (\u0058)
~~~


### 0x59 as a single-character dictionary key

Input:
~~~
Y=1
~~~

Expects Parse Error
~~~
>>Y=1<<
  ^ (0x59) Key must start with LCALPHA or '*': 'Y' (\u0059)
~~~


### 0x5a as a single-character dictionary key

Input:
~~~
Z=1
~~~

Expects Parse Error
~~~
>>Z=1<<
  ^ (0x5a) Key must start with LCALPHA or '*': 'Z' (\u005a)
~~~


### 0x5b as a single-character dictionary key

Input:
~~~
[=1
~~~

Expects Parse Error
~~~
>>[=1<<
  ^ (0x5b) Key must start with LCALPHA or '*': '[' (\u005b)
~~~


### 0x5c as a single-character dictionary key

Input:
~~~
\=1
~~~

Expects Parse Error
~~~
>>\=1<<
  ^ (0x5c) Key must start with LCALPHA or '*': '\' (\u005c)
~~~


### 0x5d as a single-character dictionary key

Input:
~~~
]=1
~~~

Expects Parse Error
~~~
>>]=1<<
  ^ (0x5d) Key must start with LCALPHA or '*': ']' (\u005d)
~~~


### 0x5e as a single-character dictionary key

Input:
~~~
^=1
~~~

Expects Parse Error
~~~
>>^=1<<
  ^ (0x5e) Key must start with LCALPHA or '*': '^' (\u005e)
~~~


### 0x5f as a single-character dictionary key

Input:
~~~
_=1
~~~

Expects Parse Error
~~~
>>_=1<<
  ^ (0x5f) Key must start with LCALPHA or '*': '_' (\u005f)
~~~


### 0x60 as a single-character dictionary key

Input:
~~~
`=1
~~~

Expects Parse Error
~~~
>>`=1<<
  ^ (0x60) Key must start with LCALPHA or '*': '`' (\u0060)
~~~


### 0x61 as a single-character dictionary key

Input:
~~~
a=1
~~~

Result:
~~~
a=1
~~~

### 0x62 as a single-character dictionary key

Input:
~~~
b=1
~~~

Result:
~~~
b=1
~~~

### 0x63 as a single-character dictionary key

Input:
~~~
c=1
~~~

Result:
~~~
c=1
~~~

### 0x64 as a single-character dictionary key

Input:
~~~
d=1
~~~

Result:
~~~
d=1
~~~

### 0x65 as a single-character dictionary key

Input:
~~~
e=1
~~~

Result:
~~~
e=1
~~~

### 0x66 as a single-character dictionary key

Input:
~~~
f=1
~~~

Result:
~~~
f=1
~~~

### 0x67 as a single-character dictionary key

Input:
~~~
g=1
~~~

Result:
~~~
g=1
~~~

### 0x68 as a single-character dictionary key

Input:
~~~
h=1
~~~

Result:
~~~
h=1
~~~

### 0x69 as a single-character dictionary key

Input:
~~~
i=1
~~~

Result:
~~~
i=1
~~~

### 0x6a as a single-character dictionary key

Input:
~~~
j=1
~~~

Result:
~~~
j=1
~~~

### 0x6b as a single-character dictionary key

Input:
~~~
k=1
~~~

Result:
~~~
k=1
~~~

### 0x6c as a single-character dictionary key

Input:
~~~
l=1
~~~

Result:
~~~
l=1
~~~

### 0x6d as a single-character dictionary key

Input:
~~~
m=1
~~~

Result:
~~~
m=1
~~~

### 0x6e as a single-character dictionary key

Input:
~~~
n=1
~~~

Result:
~~~
n=1
~~~

### 0x6f as a single-character dictionary key

Input:
~~~
o=1
~~~

Result:
~~~
o=1
~~~

### 0x70 as a single-character dictionary key

Input:
~~~
p=1
~~~

Result:
~~~
p=1
~~~

### 0x71 as a single-character dictionary key

Input:
~~~
q=1
~~~

Result:
~~~
q=1
~~~

### 0x72 as a single-character dictionary key

Input:
~~~
r=1
~~~

Result:
~~~
r=1
~~~

### 0x73 as a single-character dictionary key

Input:
~~~
s=1
~~~

Result:
~~~
s=1
~~~

### 0x74 as a single-character dictionary key

Input:
~~~
t=1
~~~

Result:
~~~
t=1
~~~

### 0x75 as a single-character dictionary key

Input:
~~~
u=1
~~~

Result:
~~~
u=1
~~~

### 0x76 as a single-character dictionary key

Input:
~~~
v=1
~~~

Result:
~~~
v=1
~~~

### 0x77 as a single-character dictionary key

Input:
~~~
w=1
~~~

Result:
~~~
w=1
~~~

### 0x78 as a single-character dictionary key

Input:
~~~
x=1
~~~

Result:
~~~
x=1
~~~

### 0x79 as a single-character dictionary key

Input:
~~~
y=1
~~~

Result:
~~~
y=1
~~~

### 0x7a as a single-character dictionary key

Input:
~~~
z=1
~~~

Result:
~~~
z=1
~~~

### 0x7b as a single-character dictionary key

Input:
~~~
{=1
~~~

Expects Parse Error
~~~
>>{=1<<
  ^ (0x7b) Key must start with LCALPHA or '*': '{' (\u007b)
~~~


### 0x7c as a single-character dictionary key

Input:
~~~
|=1
~~~

Expects Parse Error
~~~
>>|=1<<
  ^ (0x7c) Key must start with LCALPHA or '*': '|' (\u007c)
~~~


### 0x7d as a single-character dictionary key

Input:
~~~
}=1
~~~

Expects Parse Error
~~~
>>}=1<<
  ^ (0x7d) Key must start with LCALPHA or '*': '}' (\u007d)
~~~


### 0x7e as a single-character dictionary key

Input:
~~~
~=1
~~~

Expects Parse Error
~~~
>>~=1<<
  ^ (0x7e) Key must start with LCALPHA or '*': '~' (\u007e)
~~~


### 0x7f as a single-character dictionary key

Input:
~~~
=1
~~~

Expects Parse Error
~~~
>>=1<<
  ^ (0x7f) Key must start with LCALPHA or '*': '' (\u007f)
~~~


### 0x00 in dictionary key

Input:
~~~
a a=1
~~~

Expects Parse Error
~~~
>>a a=1<<
  -^ (0x00) Expected COMMA in Dictionary, found: ' ' (\u0000)
~~~


### 0x01 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x01) Expected COMMA in Dictionary, found: '' (\u0001)
~~~


### 0x02 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x02) Expected COMMA in Dictionary, found: '' (\u0002)
~~~


### 0x03 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x03) Expected COMMA in Dictionary, found: '' (\u0003)
~~~


### 0x04 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x04) Expected COMMA in Dictionary, found: '' (\u0004)
~~~


### 0x05 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x05) Expected COMMA in Dictionary, found: '' (\u0005)
~~~


### 0x06 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x06) Expected COMMA in Dictionary, found: '' (\u0006)
~~~


### 0x07 in dictionary key

Input:
~~~
a a=1
~~~

Expects Parse Error
~~~
>>a a=1<<
  -^ (0x07) Expected COMMA in Dictionary, found: ' ' (\u0007)
~~~


### 0x08 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x08) Expected COMMA in Dictionary, found: '' (\u0008)
~~~


### 0x09 in dictionary key

Input:
~~~
a	a=1
~~~

Expects Parse Error
~~~
>>a	a=1<<
  --^ (0x61) Expected COMMA in Dictionary, found: 'a' (\u0061)
~~~


### 0x0a in dictionary key

Input:
~~~
a
a=1
~~~

Expects Parse Error
~~~
>>a
a=1<<
  -^ (0x0a) Expected COMMA in Dictionary, found: '
' (\u000a)
~~~


### 0x0b in dictionary key

Input:
~~~
a
a=1
~~~

Expects Parse Error
~~~
>>a
a=1<<
  -^ (0x0b) Expected COMMA in Dictionary, found: '
' (\u000b)
~~~


### 0x0c in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x0c) Expected COMMA in Dictionary, found: '' (\u000c)
~~~


### 0x0d in dictionary key

Input:
~~~
a
a=1
~~~

Expects Parse Error
~~~
>>a
a=1<<
  -^ (0x0d) Expected COMMA in Dictionary, found: '
' (\u000d)
~~~


### 0x0e in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x0e) Expected COMMA in Dictionary, found: '' (\u000e)
~~~


### 0x0f in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x0f) Expected COMMA in Dictionary, found: '' (\u000f)
~~~


### 0x10 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x10) Expected COMMA in Dictionary, found: '' (\u0010)
~~~


### 0x11 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x11) Expected COMMA in Dictionary, found: '' (\u0011)
~~~


### 0x12 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x12) Expected COMMA in Dictionary, found: '' (\u0012)
~~~


### 0x13 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x13) Expected COMMA in Dictionary, found: '' (\u0013)
~~~


### 0x14 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x14) Expected COMMA in Dictionary, found: '' (\u0014)
~~~


### 0x15 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x15) Expected COMMA in Dictionary, found: '' (\u0015)
~~~


### 0x16 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x16) Expected COMMA in Dictionary, found: '' (\u0016)
~~~


### 0x17 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x17) Expected COMMA in Dictionary, found: '' (\u0017)
~~~


### 0x18 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x18) Expected COMMA in Dictionary, found: '' (\u0018)
~~~


### 0x19 in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x19) Expected COMMA in Dictionary, found: '' (\u0019)
~~~


### 0x1a in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x1a) Expected COMMA in Dictionary, found: '' (\u001a)
~~~


### 0x1b in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x1b) Expected COMMA in Dictionary, found: '' (\u001b)
~~~


### 0x1c in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x1c) Expected COMMA in Dictionary, found: '' (\u001c)
~~~


### 0x1d in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x1d) Expected COMMA in Dictionary, found: '' (\u001d)
~~~


### 0x1e in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x1e) Expected COMMA in Dictionary, found: '' (\u001e)
~~~


### 0x1f in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x1f) Expected COMMA in Dictionary, found: '' (\u001f)
~~~


### 0x20 in dictionary key

Input:
~~~
a a=1
~~~

Expects Parse Error
~~~
>>a a=1<<
  --^ (0x61) Expected COMMA in Dictionary, found: 'a' (\u0061)
~~~


### 0x21 in dictionary key

Input:
~~~
a!a=1
~~~

Expects Parse Error
~~~
>>a!a=1<<
  -^ (0x21) Expected COMMA in Dictionary, found: '!' (\u0021)
~~~


### 0x22 in dictionary key

Input:
~~~
a"a=1
~~~

Expects Parse Error
~~~
>>a"a=1<<
  -^ (0x22) Expected COMMA in Dictionary, found: '"' (\u0022)
~~~


### 0x23 in dictionary key

Input:
~~~
a#a=1
~~~

Expects Parse Error
~~~
>>a#a=1<<
  -^ (0x23) Expected COMMA in Dictionary, found: '#' (\u0023)
~~~


### 0x24 in dictionary key

Input:
~~~
a$a=1
~~~

Expects Parse Error
~~~
>>a$a=1<<
  -^ (0x24) Expected COMMA in Dictionary, found: '$' (\u0024)
~~~


### 0x25 in dictionary key

Input:
~~~
a%a=1
~~~

Expects Parse Error
~~~
>>a%a=1<<
  -^ (0x25) Expected COMMA in Dictionary, found: '%' (\u0025)
~~~


### 0x26 in dictionary key

Input:
~~~
a&a=1
~~~

Expects Parse Error
~~~
>>a&a=1<<
  -^ (0x26) Expected COMMA in Dictionary, found: '&' (\u0026)
~~~


### 0x27 in dictionary key

Input:
~~~
a'a=1
~~~

Expects Parse Error
~~~
>>a'a=1<<
  -^ (0x27) Expected COMMA in Dictionary, found: ''' (\u0027)
~~~


### 0x28 in dictionary key

Input:
~~~
a(a=1
~~~

Expects Parse Error
~~~
>>a(a=1<<
  -^ (0x28) Expected COMMA in Dictionary, found: '(' (\u0028)
~~~


### 0x29 in dictionary key

Input:
~~~
a)a=1
~~~

Expects Parse Error
~~~
>>a)a=1<<
  -^ (0x29) Expected COMMA in Dictionary, found: ')' (\u0029)
~~~


### 0x2a in dictionary key

Input:
~~~
a*a=1
~~~

Result:
~~~
a*a=1
~~~

### 0x2b in dictionary key

Input:
~~~
a+a=1
~~~

Expects Parse Error
~~~
>>a+a=1<<
  -^ (0x2b) Expected COMMA in Dictionary, found: '+' (\u002b)
~~~


### 0x2c in dictionary key

Input:
~~~
a,a=1
~~~

Result:
~~~
a=1
~~~

### 0x2d in dictionary key

Input:
~~~
a-a=1
~~~

Result:
~~~
a-a=1
~~~

### 0x2e in dictionary key

Input:
~~~
a.a=1
~~~

Result:
~~~
a.a=1
~~~

### 0x2f in dictionary key

Input:
~~~
a/a=1
~~~

Expects Parse Error
~~~
>>a/a=1<<
  -^ (0x2f) Expected COMMA in Dictionary, found: '/' (\u002f)
~~~


### 0x30 in dictionary key

Input:
~~~
a0a=1
~~~

Result:
~~~
a0a=1
~~~

### 0x31 in dictionary key

Input:
~~~
a1a=1
~~~

Result:
~~~
a1a=1
~~~

### 0x32 in dictionary key

Input:
~~~
a2a=1
~~~

Result:
~~~
a2a=1
~~~

### 0x33 in dictionary key

Input:
~~~
a3a=1
~~~

Result:
~~~
a3a=1
~~~

### 0x34 in dictionary key

Input:
~~~
a4a=1
~~~

Result:
~~~
a4a=1
~~~

### 0x35 in dictionary key

Input:
~~~
a5a=1
~~~

Result:
~~~
a5a=1
~~~

### 0x36 in dictionary key

Input:
~~~
a6a=1
~~~

Result:
~~~
a6a=1
~~~

### 0x37 in dictionary key

Input:
~~~
a7a=1
~~~

Result:
~~~
a7a=1
~~~

### 0x38 in dictionary key

Input:
~~~
a8a=1
~~~

Result:
~~~
a8a=1
~~~

### 0x39 in dictionary key

Input:
~~~
a9a=1
~~~

Result:
~~~
a9a=1
~~~

### 0x3a in dictionary key

Input:
~~~
a:a=1
~~~

Expects Parse Error
~~~
>>a:a=1<<
  -^ (0x3a) Expected COMMA in Dictionary, found: ':' (\u003a)
~~~


### 0x3b in dictionary key

Input:
~~~
a;a=1
~~~

Result:
~~~
a;a=1
~~~

### 0x3c in dictionary key

Input:
~~~
a<a=1
~~~

Expects Parse Error
~~~
>>a<a=1<<
  -^ (0x3c) Expected COMMA in Dictionary, found: '<' (\u003c)
~~~


### 0x3d in dictionary key

Input:
~~~
a=a=1
~~~

Expects Parse Error
~~~
>>a=a=1<<
  ---^ (0x3d) Expected COMMA in Dictionary, found: '=' (\u003d)
~~~


### 0x3e in dictionary key

Input:
~~~
a>a=1
~~~

Expects Parse Error
~~~
>>a>a=1<<
  -^ (0x3e) Expected COMMA in Dictionary, found: '>' (\u003e)
~~~


### 0x3f in dictionary key

Input:
~~~
a?a=1
~~~

Expects Parse Error
~~~
>>a?a=1<<
  -^ (0x3f) Expected COMMA in Dictionary, found: '?' (\u003f)
~~~


### 0x40 in dictionary key

Input:
~~~
a@a=1
~~~

Expects Parse Error
~~~
>>a@a=1<<
  -^ (0x40) Expected COMMA in Dictionary, found: '@' (\u0040)
~~~


### 0x41 in dictionary key

Input:
~~~
aAa=1
~~~

Expects Parse Error
~~~
>>aAa=1<<
  -^ (0x41) Expected COMMA in Dictionary, found: 'A' (\u0041)
~~~


### 0x42 in dictionary key

Input:
~~~
aBa=1
~~~

Expects Parse Error
~~~
>>aBa=1<<
  -^ (0x42) Expected COMMA in Dictionary, found: 'B' (\u0042)
~~~


### 0x43 in dictionary key

Input:
~~~
aCa=1
~~~

Expects Parse Error
~~~
>>aCa=1<<
  -^ (0x43) Expected COMMA in Dictionary, found: 'C' (\u0043)
~~~


### 0x44 in dictionary key

Input:
~~~
aDa=1
~~~

Expects Parse Error
~~~
>>aDa=1<<
  -^ (0x44) Expected COMMA in Dictionary, found: 'D' (\u0044)
~~~


### 0x45 in dictionary key

Input:
~~~
aEa=1
~~~

Expects Parse Error
~~~
>>aEa=1<<
  -^ (0x45) Expected COMMA in Dictionary, found: 'E' (\u0045)
~~~


### 0x46 in dictionary key

Input:
~~~
aFa=1
~~~

Expects Parse Error
~~~
>>aFa=1<<
  -^ (0x46) Expected COMMA in Dictionary, found: 'F' (\u0046)
~~~


### 0x47 in dictionary key

Input:
~~~
aGa=1
~~~

Expects Parse Error
~~~
>>aGa=1<<
  -^ (0x47) Expected COMMA in Dictionary, found: 'G' (\u0047)
~~~


### 0x48 in dictionary key

Input:
~~~
aHa=1
~~~

Expects Parse Error
~~~
>>aHa=1<<
  -^ (0x48) Expected COMMA in Dictionary, found: 'H' (\u0048)
~~~


### 0x49 in dictionary key

Input:
~~~
aIa=1
~~~

Expects Parse Error
~~~
>>aIa=1<<
  -^ (0x49) Expected COMMA in Dictionary, found: 'I' (\u0049)
~~~


### 0x4a in dictionary key

Input:
~~~
aJa=1
~~~

Expects Parse Error
~~~
>>aJa=1<<
  -^ (0x4a) Expected COMMA in Dictionary, found: 'J' (\u004a)
~~~


### 0x4b in dictionary key

Input:
~~~
aKa=1
~~~

Expects Parse Error
~~~
>>aKa=1<<
  -^ (0x4b) Expected COMMA in Dictionary, found: 'K' (\u004b)
~~~


### 0x4c in dictionary key

Input:
~~~
aLa=1
~~~

Expects Parse Error
~~~
>>aLa=1<<
  -^ (0x4c) Expected COMMA in Dictionary, found: 'L' (\u004c)
~~~


### 0x4d in dictionary key

Input:
~~~
aMa=1
~~~

Expects Parse Error
~~~
>>aMa=1<<
  -^ (0x4d) Expected COMMA in Dictionary, found: 'M' (\u004d)
~~~


### 0x4e in dictionary key

Input:
~~~
aNa=1
~~~

Expects Parse Error
~~~
>>aNa=1<<
  -^ (0x4e) Expected COMMA in Dictionary, found: 'N' (\u004e)
~~~


### 0x4f in dictionary key

Input:
~~~
aOa=1
~~~

Expects Parse Error
~~~
>>aOa=1<<
  -^ (0x4f) Expected COMMA in Dictionary, found: 'O' (\u004f)
~~~


### 0x50 in dictionary key

Input:
~~~
aPa=1
~~~

Expects Parse Error
~~~
>>aPa=1<<
  -^ (0x50) Expected COMMA in Dictionary, found: 'P' (\u0050)
~~~


### 0x51 in dictionary key

Input:
~~~
aQa=1
~~~

Expects Parse Error
~~~
>>aQa=1<<
  -^ (0x51) Expected COMMA in Dictionary, found: 'Q' (\u0051)
~~~


### 0x52 in dictionary key

Input:
~~~
aRa=1
~~~

Expects Parse Error
~~~
>>aRa=1<<
  -^ (0x52) Expected COMMA in Dictionary, found: 'R' (\u0052)
~~~


### 0x53 in dictionary key

Input:
~~~
aSa=1
~~~

Expects Parse Error
~~~
>>aSa=1<<
  -^ (0x53) Expected COMMA in Dictionary, found: 'S' (\u0053)
~~~


### 0x54 in dictionary key

Input:
~~~
aTa=1
~~~

Expects Parse Error
~~~
>>aTa=1<<
  -^ (0x54) Expected COMMA in Dictionary, found: 'T' (\u0054)
~~~


### 0x55 in dictionary key

Input:
~~~
aUa=1
~~~

Expects Parse Error
~~~
>>aUa=1<<
  -^ (0x55) Expected COMMA in Dictionary, found: 'U' (\u0055)
~~~


### 0x56 in dictionary key

Input:
~~~
aVa=1
~~~

Expects Parse Error
~~~
>>aVa=1<<
  -^ (0x56) Expected COMMA in Dictionary, found: 'V' (\u0056)
~~~


### 0x57 in dictionary key

Input:
~~~
aWa=1
~~~

Expects Parse Error
~~~
>>aWa=1<<
  -^ (0x57) Expected COMMA in Dictionary, found: 'W' (\u0057)
~~~


### 0x58 in dictionary key

Input:
~~~
aXa=1
~~~

Expects Parse Error
~~~
>>aXa=1<<
  -^ (0x58) Expected COMMA in Dictionary, found: 'X' (\u0058)
~~~


### 0x59 in dictionary key

Input:
~~~
aYa=1
~~~

Expects Parse Error
~~~
>>aYa=1<<
  -^ (0x59) Expected COMMA in Dictionary, found: 'Y' (\u0059)
~~~


### 0x5a in dictionary key

Input:
~~~
aZa=1
~~~

Expects Parse Error
~~~
>>aZa=1<<
  -^ (0x5a) Expected COMMA in Dictionary, found: 'Z' (\u005a)
~~~


### 0x5b in dictionary key

Input:
~~~
a[a=1
~~~

Expects Parse Error
~~~
>>a[a=1<<
  -^ (0x5b) Expected COMMA in Dictionary, found: '[' (\u005b)
~~~


### 0x5c in dictionary key

Input:
~~~
a\a=1
~~~

Expects Parse Error
~~~
>>a\a=1<<
  -^ (0x5c) Expected COMMA in Dictionary, found: '\' (\u005c)
~~~


### 0x5d in dictionary key

Input:
~~~
a]a=1
~~~

Expects Parse Error
~~~
>>a]a=1<<
  -^ (0x5d) Expected COMMA in Dictionary, found: ']' (\u005d)
~~~


### 0x5e in dictionary key

Input:
~~~
a^a=1
~~~

Expects Parse Error
~~~
>>a^a=1<<
  -^ (0x5e) Expected COMMA in Dictionary, found: '^' (\u005e)
~~~


### 0x5f in dictionary key

Input:
~~~
a_a=1
~~~

Result:
~~~
a_a=1
~~~

### 0x60 in dictionary key

Input:
~~~
a`a=1
~~~

Expects Parse Error
~~~
>>a`a=1<<
  -^ (0x60) Expected COMMA in Dictionary, found: '`' (\u0060)
~~~


### 0x61 in dictionary key

Input:
~~~
aaa=1
~~~

Result:
~~~
aaa=1
~~~

### 0x62 in dictionary key

Input:
~~~
aba=1
~~~

Result:
~~~
aba=1
~~~

### 0x63 in dictionary key

Input:
~~~
aca=1
~~~

Result:
~~~
aca=1
~~~

### 0x64 in dictionary key

Input:
~~~
ada=1
~~~

Result:
~~~
ada=1
~~~

### 0x65 in dictionary key

Input:
~~~
aea=1
~~~

Result:
~~~
aea=1
~~~

### 0x66 in dictionary key

Input:
~~~
afa=1
~~~

Result:
~~~
afa=1
~~~

### 0x67 in dictionary key

Input:
~~~
aga=1
~~~

Result:
~~~
aga=1
~~~

### 0x68 in dictionary key

Input:
~~~
aha=1
~~~

Result:
~~~
aha=1
~~~

### 0x69 in dictionary key

Input:
~~~
aia=1
~~~

Result:
~~~
aia=1
~~~

### 0x6a in dictionary key

Input:
~~~
aja=1
~~~

Result:
~~~
aja=1
~~~

### 0x6b in dictionary key

Input:
~~~
aka=1
~~~

Result:
~~~
aka=1
~~~

### 0x6c in dictionary key

Input:
~~~
ala=1
~~~

Result:
~~~
ala=1
~~~

### 0x6d in dictionary key

Input:
~~~
ama=1
~~~

Result:
~~~
ama=1
~~~

### 0x6e in dictionary key

Input:
~~~
ana=1
~~~

Result:
~~~
ana=1
~~~

### 0x6f in dictionary key

Input:
~~~
aoa=1
~~~

Result:
~~~
aoa=1
~~~

### 0x70 in dictionary key

Input:
~~~
apa=1
~~~

Result:
~~~
apa=1
~~~

### 0x71 in dictionary key

Input:
~~~
aqa=1
~~~

Result:
~~~
aqa=1
~~~

### 0x72 in dictionary key

Input:
~~~
ara=1
~~~

Result:
~~~
ara=1
~~~

### 0x73 in dictionary key

Input:
~~~
asa=1
~~~

Result:
~~~
asa=1
~~~

### 0x74 in dictionary key

Input:
~~~
ata=1
~~~

Result:
~~~
ata=1
~~~

### 0x75 in dictionary key

Input:
~~~
aua=1
~~~

Result:
~~~
aua=1
~~~

### 0x76 in dictionary key

Input:
~~~
ava=1
~~~

Result:
~~~
ava=1
~~~

### 0x77 in dictionary key

Input:
~~~
awa=1
~~~

Result:
~~~
awa=1
~~~

### 0x78 in dictionary key

Input:
~~~
axa=1
~~~

Result:
~~~
axa=1
~~~

### 0x79 in dictionary key

Input:
~~~
aya=1
~~~

Result:
~~~
aya=1
~~~

### 0x7a in dictionary key

Input:
~~~
aza=1
~~~

Result:
~~~
aza=1
~~~

### 0x7b in dictionary key

Input:
~~~
a{a=1
~~~

Expects Parse Error
~~~
>>a{a=1<<
  -^ (0x7b) Expected COMMA in Dictionary, found: '{' (\u007b)
~~~


### 0x7c in dictionary key

Input:
~~~
a|a=1
~~~

Expects Parse Error
~~~
>>a|a=1<<
  -^ (0x7c) Expected COMMA in Dictionary, found: '|' (\u007c)
~~~


### 0x7d in dictionary key

Input:
~~~
a}a=1
~~~

Expects Parse Error
~~~
>>a}a=1<<
  -^ (0x7d) Expected COMMA in Dictionary, found: '}' (\u007d)
~~~


### 0x7e in dictionary key

Input:
~~~
a~a=1
~~~

Expects Parse Error
~~~
>>a~a=1<<
  -^ (0x7e) Expected COMMA in Dictionary, found: '~' (\u007e)
~~~


### 0x7f in dictionary key

Input:
~~~
aa=1
~~~

Expects Parse Error
~~~
>>aa=1<<
  -^ (0x7f) Expected COMMA in Dictionary, found: '' (\u007f)
~~~


### 0x00 starting a dictionary key

Input:
~~~
 a=1
~~~

Expects Parse Error
~~~
>> a=1<<
  ^ (0x00) Key must start with LCALPHA or '*': ' ' (\u0000)
~~~


### 0x01 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x01) Key must start with LCALPHA or '*': '' (\u0001)
~~~


### 0x02 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x02) Key must start with LCALPHA or '*': '' (\u0002)
~~~


### 0x03 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x03) Key must start with LCALPHA or '*': '' (\u0003)
~~~


### 0x04 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x04) Key must start with LCALPHA or '*': '' (\u0004)
~~~


### 0x05 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x05) Key must start with LCALPHA or '*': '' (\u0005)
~~~


### 0x06 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x06) Key must start with LCALPHA or '*': '' (\u0006)
~~~


### 0x07 starting a dictionary key

Input:
~~~
 a=1
~~~

Expects Parse Error
~~~
>> a=1<<
  ^ (0x07) Key must start with LCALPHA or '*': ' ' (\u0007)
~~~


### 0x08 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x08) Key must start with LCALPHA or '*': '' (\u0008)
~~~


### 0x09 starting a dictionary key

Input:
~~~
	a=1
~~~

Expects Parse Error
~~~
>>	a=1<<
  ^ (0x09) Key must start with LCALPHA or '*': HTAB (\u0009)
~~~


### 0x0a starting a dictionary key

Input:
~~~

a=1
~~~

Expects Parse Error
~~~
>>
a=1<<
  ^ (0x0a) Key must start with LCALPHA or '*': '
' (\u000a)
~~~


### 0x0b starting a dictionary key

Input:
~~~

a=1
~~~

Expects Parse Error
~~~
>>
a=1<<
  ^ (0x0b) Key must start with LCALPHA or '*': '
' (\u000b)
~~~


### 0x0c starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x0c) Key must start with LCALPHA or '*': '' (\u000c)
~~~


### 0x0d starting a dictionary key

Input:
~~~

a=1
~~~

Expects Parse Error
~~~
>>
a=1<<
  ^ (0x0d) Key must start with LCALPHA or '*': '
' (\u000d)
~~~


### 0x0e starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x0e) Key must start with LCALPHA or '*': '' (\u000e)
~~~


### 0x0f starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x0f) Key must start with LCALPHA or '*': '' (\u000f)
~~~


### 0x10 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x10) Key must start with LCALPHA or '*': '' (\u0010)
~~~


### 0x11 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x11) Key must start with LCALPHA or '*': '' (\u0011)
~~~


### 0x12 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x12) Key must start with LCALPHA or '*': '' (\u0012)
~~~


### 0x13 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x13) Key must start with LCALPHA or '*': '' (\u0013)
~~~


### 0x14 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x14) Key must start with LCALPHA or '*': '' (\u0014)
~~~


### 0x15 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x15) Key must start with LCALPHA or '*': '' (\u0015)
~~~


### 0x16 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x16) Key must start with LCALPHA or '*': '' (\u0016)
~~~


### 0x17 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x17) Key must start with LCALPHA or '*': '' (\u0017)
~~~


### 0x18 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x18) Key must start with LCALPHA or '*': '' (\u0018)
~~~


### 0x19 starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x19) Key must start with LCALPHA or '*': '' (\u0019)
~~~


### 0x1a starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x1a) Key must start with LCALPHA or '*': '' (\u001a)
~~~


### 0x1b starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x1b) Key must start with LCALPHA or '*': '' (\u001b)
~~~


### 0x1c starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x1c) Key must start with LCALPHA or '*': '' (\u001c)
~~~


### 0x1d starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x1d) Key must start with LCALPHA or '*': '' (\u001d)
~~~


### 0x1e starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x1e) Key must start with LCALPHA or '*': '' (\u001e)
~~~


### 0x1f starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x1f) Key must start with LCALPHA or '*': '' (\u001f)
~~~


### 0x20 starting a dictionary key

Input:
~~~
 a=1
~~~

Result:
~~~
a=1
~~~

### 0x21 starting a dictionary key

Input:
~~~
!a=1
~~~

Expects Parse Error
~~~
>>!a=1<<
  ^ (0x21) Key must start with LCALPHA or '*': '!' (\u0021)
~~~


### 0x22 starting a dictionary key

Input:
~~~
"a=1
~~~

Expects Parse Error
~~~
>>"a=1<<
  ^ (0x22) Key must start with LCALPHA or '*': '"' (\u0022)
~~~


### 0x23 starting a dictionary key

Input:
~~~
#a=1
~~~

Expects Parse Error
~~~
>>#a=1<<
  ^ (0x23) Key must start with LCALPHA or '*': '#' (\u0023)
~~~


### 0x24 starting a dictionary key

Input:
~~~
$a=1
~~~

Expects Parse Error
~~~
>>$a=1<<
  ^ (0x24) Key must start with LCALPHA or '*': '$' (\u0024)
~~~


### 0x25 starting a dictionary key

Input:
~~~
%a=1
~~~

Expects Parse Error
~~~
>>%a=1<<
  ^ (0x25) Key must start with LCALPHA or '*': '%' (\u0025)
~~~


### 0x26 starting a dictionary key

Input:
~~~
&a=1
~~~

Expects Parse Error
~~~
>>&a=1<<
  ^ (0x26) Key must start with LCALPHA or '*': '&' (\u0026)
~~~


### 0x27 starting a dictionary key

Input:
~~~
'a=1
~~~

Expects Parse Error
~~~
>>'a=1<<
  ^ (0x27) Key must start with LCALPHA or '*': ''' (\u0027)
~~~


### 0x28 starting a dictionary key

Input:
~~~
(a=1
~~~

Expects Parse Error
~~~
>>(a=1<<
  ^ (0x28) Key must start with LCALPHA or '*': '(' (\u0028)
~~~


### 0x29 starting a dictionary key

Input:
~~~
)a=1
~~~

Expects Parse Error
~~~
>>)a=1<<
  ^ (0x29) Key must start with LCALPHA or '*': ')' (\u0029)
~~~


### 0x2a starting a dictionary key

Input:
~~~
*a=1
~~~

Result:
~~~
*a=1
~~~

### 0x2b starting a dictionary key

Input:
~~~
+a=1
~~~

Expects Parse Error
~~~
>>+a=1<<
  ^ (0x2b) Key must start with LCALPHA or '*': '+' (\u002b)
~~~


### 0x2c starting a dictionary key

Input:
~~~
,a=1
~~~

Expects Parse Error
~~~
>>,a=1<<
  ^ (0x2c) Key must start with LCALPHA or '*': ',' (\u002c)
~~~


### 0x2d starting a dictionary key

Input:
~~~
-a=1
~~~

Expects Parse Error
~~~
>>-a=1<<
  ^ (0x2d) Key must start with LCALPHA or '*': '-' (\u002d)
~~~


### 0x2e starting a dictionary key

Input:
~~~
.a=1
~~~

Expects Parse Error
~~~
>>.a=1<<
  ^ (0x2e) Key must start with LCALPHA or '*': '.' (\u002e)
~~~


### 0x2f starting a dictionary key

Input:
~~~
/a=1
~~~

Expects Parse Error
~~~
>>/a=1<<
  ^ (0x2f) Key must start with LCALPHA or '*': '/' (\u002f)
~~~


### 0x30 starting a dictionary key

Input:
~~~
0a=1
~~~

Expects Parse Error
~~~
>>0a=1<<
  ^ (0x30) Key must start with LCALPHA or '*': '0' (\u0030)
~~~


### 0x31 starting a dictionary key

Input:
~~~
1a=1
~~~

Expects Parse Error
~~~
>>1a=1<<
  ^ (0x31) Key must start with LCALPHA or '*': '1' (\u0031)
~~~


### 0x32 starting a dictionary key

Input:
~~~
2a=1
~~~

Expects Parse Error
~~~
>>2a=1<<
  ^ (0x32) Key must start with LCALPHA or '*': '2' (\u0032)
~~~


### 0x33 starting a dictionary key

Input:
~~~
3a=1
~~~

Expects Parse Error
~~~
>>3a=1<<
  ^ (0x33) Key must start with LCALPHA or '*': '3' (\u0033)
~~~


### 0x34 starting a dictionary key

Input:
~~~
4a=1
~~~

Expects Parse Error
~~~
>>4a=1<<
  ^ (0x34) Key must start with LCALPHA or '*': '4' (\u0034)
~~~


### 0x35 starting a dictionary key

Input:
~~~
5a=1
~~~

Expects Parse Error
~~~
>>5a=1<<
  ^ (0x35) Key must start with LCALPHA or '*': '5' (\u0035)
~~~


### 0x36 starting a dictionary key

Input:
~~~
6a=1
~~~

Expects Parse Error
~~~
>>6a=1<<
  ^ (0x36) Key must start with LCALPHA or '*': '6' (\u0036)
~~~


### 0x37 starting a dictionary key

Input:
~~~
7a=1
~~~

Expects Parse Error
~~~
>>7a=1<<
  ^ (0x37) Key must start with LCALPHA or '*': '7' (\u0037)
~~~


### 0x38 starting a dictionary key

Input:
~~~
8a=1
~~~

Expects Parse Error
~~~
>>8a=1<<
  ^ (0x38) Key must start with LCALPHA or '*': '8' (\u0038)
~~~


### 0x39 starting a dictionary key

Input:
~~~
9a=1
~~~

Expects Parse Error
~~~
>>9a=1<<
  ^ (0x39) Key must start with LCALPHA or '*': '9' (\u0039)
~~~


### 0x3a starting a dictionary key

Input:
~~~
:a=1
~~~

Expects Parse Error
~~~
>>:a=1<<
  ^ (0x3a) Key must start with LCALPHA or '*': ':' (\u003a)
~~~


### 0x3b starting a dictionary key

Input:
~~~
;a=1
~~~

Expects Parse Error
~~~
>>;a=1<<
  ^ (0x3b) Key must start with LCALPHA or '*': ';' (\u003b)
~~~


### 0x3c starting a dictionary key

Input:
~~~
<a=1
~~~

Expects Parse Error
~~~
>><a=1<<
  ^ (0x3c) Key must start with LCALPHA or '*': '<' (\u003c)
~~~


### 0x3d starting a dictionary key

Input:
~~~
=a=1
~~~

Expects Parse Error
~~~
>>=a=1<<
  ^ (0x3d) Key must start with LCALPHA or '*': '=' (\u003d)
~~~


### 0x3e starting a dictionary key

Input:
~~~
>a=1
~~~

Expects Parse Error
~~~
>>>a=1<<
  ^ (0x3e) Key must start with LCALPHA or '*': '>' (\u003e)
~~~


### 0x3f starting a dictionary key

Input:
~~~
?a=1
~~~

Expects Parse Error
~~~
>>?a=1<<
  ^ (0x3f) Key must start with LCALPHA or '*': '?' (\u003f)
~~~


### 0x40 starting a dictionary key

Input:
~~~
@a=1
~~~

Expects Parse Error
~~~
>>@a=1<<
  ^ (0x40) Key must start with LCALPHA or '*': '@' (\u0040)
~~~


### 0x41 starting a dictionary key

Input:
~~~
Aa=1
~~~

Expects Parse Error
~~~
>>Aa=1<<
  ^ (0x41) Key must start with LCALPHA or '*': 'A' (\u0041)
~~~


### 0x42 starting a dictionary key

Input:
~~~
Ba=1
~~~

Expects Parse Error
~~~
>>Ba=1<<
  ^ (0x42) Key must start with LCALPHA or '*': 'B' (\u0042)
~~~


### 0x43 starting a dictionary key

Input:
~~~
Ca=1
~~~

Expects Parse Error
~~~
>>Ca=1<<
  ^ (0x43) Key must start with LCALPHA or '*': 'C' (\u0043)
~~~


### 0x44 starting a dictionary key

Input:
~~~
Da=1
~~~

Expects Parse Error
~~~
>>Da=1<<
  ^ (0x44) Key must start with LCALPHA or '*': 'D' (\u0044)
~~~


### 0x45 starting a dictionary key

Input:
~~~
Ea=1
~~~

Expects Parse Error
~~~
>>Ea=1<<
  ^ (0x45) Key must start with LCALPHA or '*': 'E' (\u0045)
~~~


### 0x46 starting a dictionary key

Input:
~~~
Fa=1
~~~

Expects Parse Error
~~~
>>Fa=1<<
  ^ (0x46) Key must start with LCALPHA or '*': 'F' (\u0046)
~~~


### 0x47 starting a dictionary key

Input:
~~~
Ga=1
~~~

Expects Parse Error
~~~
>>Ga=1<<
  ^ (0x47) Key must start with LCALPHA or '*': 'G' (\u0047)
~~~


### 0x48 starting a dictionary key

Input:
~~~
Ha=1
~~~

Expects Parse Error
~~~
>>Ha=1<<
  ^ (0x48) Key must start with LCALPHA or '*': 'H' (\u0048)
~~~


### 0x49 starting a dictionary key

Input:
~~~
Ia=1
~~~

Expects Parse Error
~~~
>>Ia=1<<
  ^ (0x49) Key must start with LCALPHA or '*': 'I' (\u0049)
~~~


### 0x4a starting a dictionary key

Input:
~~~
Ja=1
~~~

Expects Parse Error
~~~
>>Ja=1<<
  ^ (0x4a) Key must start with LCALPHA or '*': 'J' (\u004a)
~~~


### 0x4b starting a dictionary key

Input:
~~~
Ka=1
~~~

Expects Parse Error
~~~
>>Ka=1<<
  ^ (0x4b) Key must start with LCALPHA or '*': 'K' (\u004b)
~~~


### 0x4c starting a dictionary key

Input:
~~~
La=1
~~~

Expects Parse Error
~~~
>>La=1<<
  ^ (0x4c) Key must start with LCALPHA or '*': 'L' (\u004c)
~~~


### 0x4d starting a dictionary key

Input:
~~~
Ma=1
~~~

Expects Parse Error
~~~
>>Ma=1<<
  ^ (0x4d) Key must start with LCALPHA or '*': 'M' (\u004d)
~~~


### 0x4e starting a dictionary key

Input:
~~~
Na=1
~~~

Expects Parse Error
~~~
>>Na=1<<
  ^ (0x4e) Key must start with LCALPHA or '*': 'N' (\u004e)
~~~


### 0x4f starting a dictionary key

Input:
~~~
Oa=1
~~~

Expects Parse Error
~~~
>>Oa=1<<
  ^ (0x4f) Key must start with LCALPHA or '*': 'O' (\u004f)
~~~


### 0x50 starting a dictionary key

Input:
~~~
Pa=1
~~~

Expects Parse Error
~~~
>>Pa=1<<
  ^ (0x50) Key must start with LCALPHA or '*': 'P' (\u0050)
~~~


### 0x51 starting a dictionary key

Input:
~~~
Qa=1
~~~

Expects Parse Error
~~~
>>Qa=1<<
  ^ (0x51) Key must start with LCALPHA or '*': 'Q' (\u0051)
~~~


### 0x52 starting a dictionary key

Input:
~~~
Ra=1
~~~

Expects Parse Error
~~~
>>Ra=1<<
  ^ (0x52) Key must start with LCALPHA or '*': 'R' (\u0052)
~~~


### 0x53 starting a dictionary key

Input:
~~~
Sa=1
~~~

Expects Parse Error
~~~
>>Sa=1<<
  ^ (0x53) Key must start with LCALPHA or '*': 'S' (\u0053)
~~~


### 0x54 starting a dictionary key

Input:
~~~
Ta=1
~~~

Expects Parse Error
~~~
>>Ta=1<<
  ^ (0x54) Key must start with LCALPHA or '*': 'T' (\u0054)
~~~


### 0x55 starting a dictionary key

Input:
~~~
Ua=1
~~~

Expects Parse Error
~~~
>>Ua=1<<
  ^ (0x55) Key must start with LCALPHA or '*': 'U' (\u0055)
~~~


### 0x56 starting a dictionary key

Input:
~~~
Va=1
~~~

Expects Parse Error
~~~
>>Va=1<<
  ^ (0x56) Key must start with LCALPHA or '*': 'V' (\u0056)
~~~


### 0x57 starting a dictionary key

Input:
~~~
Wa=1
~~~

Expects Parse Error
~~~
>>Wa=1<<
  ^ (0x57) Key must start with LCALPHA or '*': 'W' (\u0057)
~~~


### 0x58 starting a dictionary key

Input:
~~~
Xa=1
~~~

Expects Parse Error
~~~
>>Xa=1<<
  ^ (0x58) Key must start with LCALPHA or '*': 'X' (\u0058)
~~~


### 0x59 starting a dictionary key

Input:
~~~
Ya=1
~~~

Expects Parse Error
~~~
>>Ya=1<<
  ^ (0x59) Key must start with LCALPHA or '*': 'Y' (\u0059)
~~~


### 0x5a starting a dictionary key

Input:
~~~
Za=1
~~~

Expects Parse Error
~~~
>>Za=1<<
  ^ (0x5a) Key must start with LCALPHA or '*': 'Z' (\u005a)
~~~


### 0x5b starting a dictionary key

Input:
~~~
[a=1
~~~

Expects Parse Error
~~~
>>[a=1<<
  ^ (0x5b) Key must start with LCALPHA or '*': '[' (\u005b)
~~~


### 0x5c starting a dictionary key

Input:
~~~
\a=1
~~~

Expects Parse Error
~~~
>>\a=1<<
  ^ (0x5c) Key must start with LCALPHA or '*': '\' (\u005c)
~~~


### 0x5d starting a dictionary key

Input:
~~~
]a=1
~~~

Expects Parse Error
~~~
>>]a=1<<
  ^ (0x5d) Key must start with LCALPHA or '*': ']' (\u005d)
~~~


### 0x5e starting a dictionary key

Input:
~~~
^a=1
~~~

Expects Parse Error
~~~
>>^a=1<<
  ^ (0x5e) Key must start with LCALPHA or '*': '^' (\u005e)
~~~


### 0x5f starting a dictionary key

Input:
~~~
_a=1
~~~

Expects Parse Error
~~~
>>_a=1<<
  ^ (0x5f) Key must start with LCALPHA or '*': '_' (\u005f)
~~~


### 0x60 starting a dictionary key

Input:
~~~
`a=1
~~~

Expects Parse Error
~~~
>>`a=1<<
  ^ (0x60) Key must start with LCALPHA or '*': '`' (\u0060)
~~~


### 0x61 starting a dictionary key

Input:
~~~
aa=1
~~~

Result:
~~~
aa=1
~~~

### 0x62 starting a dictionary key

Input:
~~~
ba=1
~~~

Result:
~~~
ba=1
~~~

### 0x63 starting a dictionary key

Input:
~~~
ca=1
~~~

Result:
~~~
ca=1
~~~

### 0x64 starting a dictionary key

Input:
~~~
da=1
~~~

Result:
~~~
da=1
~~~

### 0x65 starting a dictionary key

Input:
~~~
ea=1
~~~

Result:
~~~
ea=1
~~~

### 0x66 starting a dictionary key

Input:
~~~
fa=1
~~~

Result:
~~~
fa=1
~~~

### 0x67 starting a dictionary key

Input:
~~~
ga=1
~~~

Result:
~~~
ga=1
~~~

### 0x68 starting a dictionary key

Input:
~~~
ha=1
~~~

Result:
~~~
ha=1
~~~

### 0x69 starting a dictionary key

Input:
~~~
ia=1
~~~

Result:
~~~
ia=1
~~~

### 0x6a starting a dictionary key

Input:
~~~
ja=1
~~~

Result:
~~~
ja=1
~~~

### 0x6b starting a dictionary key

Input:
~~~
ka=1
~~~

Result:
~~~
ka=1
~~~

### 0x6c starting a dictionary key

Input:
~~~
la=1
~~~

Result:
~~~
la=1
~~~

### 0x6d starting a dictionary key

Input:
~~~
ma=1
~~~

Result:
~~~
ma=1
~~~

### 0x6e starting a dictionary key

Input:
~~~
na=1
~~~

Result:
~~~
na=1
~~~

### 0x6f starting a dictionary key

Input:
~~~
oa=1
~~~

Result:
~~~
oa=1
~~~

### 0x70 starting a dictionary key

Input:
~~~
pa=1
~~~

Result:
~~~
pa=1
~~~

### 0x71 starting a dictionary key

Input:
~~~
qa=1
~~~

Result:
~~~
qa=1
~~~

### 0x72 starting a dictionary key

Input:
~~~
ra=1
~~~

Result:
~~~
ra=1
~~~

### 0x73 starting a dictionary key

Input:
~~~
sa=1
~~~

Result:
~~~
sa=1
~~~

### 0x74 starting a dictionary key

Input:
~~~
ta=1
~~~

Result:
~~~
ta=1
~~~

### 0x75 starting a dictionary key

Input:
~~~
ua=1
~~~

Result:
~~~
ua=1
~~~

### 0x76 starting a dictionary key

Input:
~~~
va=1
~~~

Result:
~~~
va=1
~~~

### 0x77 starting a dictionary key

Input:
~~~
wa=1
~~~

Result:
~~~
wa=1
~~~

### 0x78 starting a dictionary key

Input:
~~~
xa=1
~~~

Result:
~~~
xa=1
~~~

### 0x79 starting a dictionary key

Input:
~~~
ya=1
~~~

Result:
~~~
ya=1
~~~

### 0x7a starting a dictionary key

Input:
~~~
za=1
~~~

Result:
~~~
za=1
~~~

### 0x7b starting a dictionary key

Input:
~~~
{a=1
~~~

Expects Parse Error
~~~
>>{a=1<<
  ^ (0x7b) Key must start with LCALPHA or '*': '{' (\u007b)
~~~


### 0x7c starting a dictionary key

Input:
~~~
|a=1
~~~

Expects Parse Error
~~~
>>|a=1<<
  ^ (0x7c) Key must start with LCALPHA or '*': '|' (\u007c)
~~~


### 0x7d starting a dictionary key

Input:
~~~
}a=1
~~~

Expects Parse Error
~~~
>>}a=1<<
  ^ (0x7d) Key must start with LCALPHA or '*': '}' (\u007d)
~~~


### 0x7e starting a dictionary key

Input:
~~~
~a=1
~~~

Expects Parse Error
~~~
>>~a=1<<
  ^ (0x7e) Key must start with LCALPHA or '*': '~' (\u007e)
~~~


### 0x7f starting a dictionary key

Input:
~~~
a=1
~~~

Expects Parse Error
~~~
>>a=1<<
  ^ (0x7f) Key must start with LCALPHA or '*': '' (\u007f)
~~~


### 0x00 in parameterised list key

Input:
~~~
foo; a a=1
~~~

Expects Parse Error
~~~
>>foo; a a=1<<
  ------^ (0x00) Expected COMMA in List, got: ' ' (\u0000)
~~~


### 0x01 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x01) Expected COMMA in List, got: '' (\u0001)
~~~


### 0x02 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x02) Expected COMMA in List, got: '' (\u0002)
~~~


### 0x03 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x03) Expected COMMA in List, got: '' (\u0003)
~~~


### 0x04 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x04) Expected COMMA in List, got: '' (\u0004)
~~~


### 0x05 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x05) Expected COMMA in List, got: '' (\u0005)
~~~


### 0x06 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x06) Expected COMMA in List, got: '' (\u0006)
~~~


### 0x07 in parameterised list key

Input:
~~~
foo; a a=1
~~~

Expects Parse Error
~~~
>>foo; a a=1<<
  ------^ (0x07) Expected COMMA in List, got: ' ' (\u0007)
~~~


### 0x08 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x08) Expected COMMA in List, got: '' (\u0008)
~~~


### 0x09 in parameterised list key

Input:
~~~
foo; a	a=1
~~~

Expects Parse Error
~~~
>>foo; a	a=1<<
  -------^ (0x61) Expected COMMA in List, got: 'a' (\u0061)
~~~


### 0x0a in parameterised list key

Input:
~~~
foo; a
a=1
~~~

Expects Parse Error
~~~
>>foo; a
a=1<<
  ------^ (0x0a) Expected COMMA in List, got: '
' (\u000a)
~~~


### 0x0b in parameterised list key

Input:
~~~
foo; a
a=1
~~~

Expects Parse Error
~~~
>>foo; a
a=1<<
  ------^ (0x0b) Expected COMMA in List, got: '
' (\u000b)
~~~


### 0x0c in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x0c) Expected COMMA in List, got: '' (\u000c)
~~~


### 0x0d in parameterised list key

Input:
~~~
foo; a
a=1
~~~

Expects Parse Error
~~~
>>foo; a
a=1<<
  ------^ (0x0d) Expected COMMA in List, got: '
' (\u000d)
~~~


### 0x0e in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x0e) Expected COMMA in List, got: '' (\u000e)
~~~


### 0x0f in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x0f) Expected COMMA in List, got: '' (\u000f)
~~~


### 0x10 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x10) Expected COMMA in List, got: '' (\u0010)
~~~


### 0x11 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x11) Expected COMMA in List, got: '' (\u0011)
~~~


### 0x12 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x12) Expected COMMA in List, got: '' (\u0012)
~~~


### 0x13 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x13) Expected COMMA in List, got: '' (\u0013)
~~~


### 0x14 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x14) Expected COMMA in List, got: '' (\u0014)
~~~


### 0x15 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x15) Expected COMMA in List, got: '' (\u0015)
~~~


### 0x16 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x16) Expected COMMA in List, got: '' (\u0016)
~~~


### 0x17 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x17) Expected COMMA in List, got: '' (\u0017)
~~~


### 0x18 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x18) Expected COMMA in List, got: '' (\u0018)
~~~


### 0x19 in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x19) Expected COMMA in List, got: '' (\u0019)
~~~


### 0x1a in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x1a) Expected COMMA in List, got: '' (\u001a)
~~~


### 0x1b in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x1b) Expected COMMA in List, got: '' (\u001b)
~~~


### 0x1c in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x1c) Expected COMMA in List, got: '' (\u001c)
~~~


### 0x1d in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x1d) Expected COMMA in List, got: '' (\u001d)
~~~


### 0x1e in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x1e) Expected COMMA in List, got: '' (\u001e)
~~~


### 0x1f in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x1f) Expected COMMA in List, got: '' (\u001f)
~~~


### 0x20 in parameterised list key

Input:
~~~
foo; a a=1
~~~

Expects Parse Error
~~~
>>foo; a a=1<<
  -------^ (0x61) Expected COMMA in List, got: 'a' (\u0061)
~~~


### 0x21 in parameterised list key

Input:
~~~
foo; a!a=1
~~~

Expects Parse Error
~~~
>>foo; a!a=1<<
  ------^ (0x21) Expected COMMA in List, got: '!' (\u0021)
~~~


### 0x22 in parameterised list key

Input:
~~~
foo; a"a=1
~~~

Expects Parse Error
~~~
>>foo; a"a=1<<
  ------^ (0x22) Expected COMMA in List, got: '"' (\u0022)
~~~


### 0x23 in parameterised list key

Input:
~~~
foo; a#a=1
~~~

Expects Parse Error
~~~
>>foo; a#a=1<<
  ------^ (0x23) Expected COMMA in List, got: '#' (\u0023)
~~~


### 0x24 in parameterised list key

Input:
~~~
foo; a$a=1
~~~

Expects Parse Error
~~~
>>foo; a$a=1<<
  ------^ (0x24) Expected COMMA in List, got: '$' (\u0024)
~~~


### 0x25 in parameterised list key

Input:
~~~
foo; a%a=1
~~~

Expects Parse Error
~~~
>>foo; a%a=1<<
  ------^ (0x25) Expected COMMA in List, got: '%' (\u0025)
~~~


### 0x26 in parameterised list key

Input:
~~~
foo; a&a=1
~~~

Expects Parse Error
~~~
>>foo; a&a=1<<
  ------^ (0x26) Expected COMMA in List, got: '&' (\u0026)
~~~


### 0x27 in parameterised list key

Input:
~~~
foo; a'a=1
~~~

Expects Parse Error
~~~
>>foo; a'a=1<<
  ------^ (0x27) Expected COMMA in List, got: ''' (\u0027)
~~~


### 0x28 in parameterised list key

Input:
~~~
foo; a(a=1
~~~

Expects Parse Error
~~~
>>foo; a(a=1<<
  ------^ (0x28) Expected COMMA in List, got: '(' (\u0028)
~~~


### 0x29 in parameterised list key

Input:
~~~
foo; a)a=1
~~~

Expects Parse Error
~~~
>>foo; a)a=1<<
  ------^ (0x29) Expected COMMA in List, got: ')' (\u0029)
~~~


### 0x2a in parameterised list key

Input:
~~~
foo; a*a=1
~~~

Result:
~~~
foo;a*a=1
~~~

### 0x2b in parameterised list key

Input:
~~~
foo; a+a=1
~~~

Expects Parse Error
~~~
>>foo; a+a=1<<
  ------^ (0x2b) Expected COMMA in List, got: '+' (\u002b)
~~~


### 0x2c in parameterised list key

Input:
~~~
foo; a,a=1
~~~

Expects Parse Error
~~~
>>foo; a,a=1<<
  --------^ (0x3d) Expected COMMA in List, got: '=' (\u003d)
~~~


### 0x2d in parameterised list key

Input:
~~~
foo; a-a=1
~~~

Result:
~~~
foo;a-a=1
~~~

### 0x2e in parameterised list key

Input:
~~~
foo; a.a=1
~~~

Result:
~~~
foo;a.a=1
~~~

### 0x2f in parameterised list key

Input:
~~~
foo; a/a=1
~~~

Expects Parse Error
~~~
>>foo; a/a=1<<
  ------^ (0x2f) Expected COMMA in List, got: '/' (\u002f)
~~~


### 0x30 in parameterised list key

Input:
~~~
foo; a0a=1
~~~

Result:
~~~
foo;a0a=1
~~~

### 0x31 in parameterised list key

Input:
~~~
foo; a1a=1
~~~

Result:
~~~
foo;a1a=1
~~~

### 0x32 in parameterised list key

Input:
~~~
foo; a2a=1
~~~

Result:
~~~
foo;a2a=1
~~~

### 0x33 in parameterised list key

Input:
~~~
foo; a3a=1
~~~

Result:
~~~
foo;a3a=1
~~~

### 0x34 in parameterised list key

Input:
~~~
foo; a4a=1
~~~

Result:
~~~
foo;a4a=1
~~~

### 0x35 in parameterised list key

Input:
~~~
foo; a5a=1
~~~

Result:
~~~
foo;a5a=1
~~~

### 0x36 in parameterised list key

Input:
~~~
foo; a6a=1
~~~

Result:
~~~
foo;a6a=1
~~~

### 0x37 in parameterised list key

Input:
~~~
foo; a7a=1
~~~

Result:
~~~
foo;a7a=1
~~~

### 0x38 in parameterised list key

Input:
~~~
foo; a8a=1
~~~

Result:
~~~
foo;a8a=1
~~~

### 0x39 in parameterised list key

Input:
~~~
foo; a9a=1
~~~

Result:
~~~
foo;a9a=1
~~~

### 0x3a in parameterised list key

Input:
~~~
foo; a:a=1
~~~

Expects Parse Error
~~~
>>foo; a:a=1<<
  ------^ (0x3a) Expected COMMA in List, got: ':' (\u003a)
~~~


### 0x3b in parameterised list key

Input:
~~~
foo; a;a=1
~~~

Result:
~~~
foo;a=1
~~~

### 0x3c in parameterised list key

Input:
~~~
foo; a<a=1
~~~

Expects Parse Error
~~~
>>foo; a<a=1<<
  ------^ (0x3c) Expected COMMA in List, got: '<' (\u003c)
~~~


### 0x3d in parameterised list key

Input:
~~~
foo; a=a=1
~~~

Expects Parse Error
~~~
>>foo; a=a=1<<
  --------^ (0x3d) Expected COMMA in List, got: '=' (\u003d)
~~~


### 0x3e in parameterised list key

Input:
~~~
foo; a>a=1
~~~

Expects Parse Error
~~~
>>foo; a>a=1<<
  ------^ (0x3e) Expected COMMA in List, got: '>' (\u003e)
~~~


### 0x3f in parameterised list key

Input:
~~~
foo; a?a=1
~~~

Expects Parse Error
~~~
>>foo; a?a=1<<
  ------^ (0x3f) Expected COMMA in List, got: '?' (\u003f)
~~~


### 0x40 in parameterised list key

Input:
~~~
foo; a@a=1
~~~

Expects Parse Error
~~~
>>foo; a@a=1<<
  ------^ (0x40) Expected COMMA in List, got: '@' (\u0040)
~~~


### 0x41 in parameterised list key

Input:
~~~
foo; aAa=1
~~~

Expects Parse Error
~~~
>>foo; aAa=1<<
  ------^ (0x41) Expected COMMA in List, got: 'A' (\u0041)
~~~


### 0x42 in parameterised list key

Input:
~~~
foo; aBa=1
~~~

Expects Parse Error
~~~
>>foo; aBa=1<<
  ------^ (0x42) Expected COMMA in List, got: 'B' (\u0042)
~~~


### 0x43 in parameterised list key

Input:
~~~
foo; aCa=1
~~~

Expects Parse Error
~~~
>>foo; aCa=1<<
  ------^ (0x43) Expected COMMA in List, got: 'C' (\u0043)
~~~


### 0x44 in parameterised list key

Input:
~~~
foo; aDa=1
~~~

Expects Parse Error
~~~
>>foo; aDa=1<<
  ------^ (0x44) Expected COMMA in List, got: 'D' (\u0044)
~~~


### 0x45 in parameterised list key

Input:
~~~
foo; aEa=1
~~~

Expects Parse Error
~~~
>>foo; aEa=1<<
  ------^ (0x45) Expected COMMA in List, got: 'E' (\u0045)
~~~


### 0x46 in parameterised list key

Input:
~~~
foo; aFa=1
~~~

Expects Parse Error
~~~
>>foo; aFa=1<<
  ------^ (0x46) Expected COMMA in List, got: 'F' (\u0046)
~~~


### 0x47 in parameterised list key

Input:
~~~
foo; aGa=1
~~~

Expects Parse Error
~~~
>>foo; aGa=1<<
  ------^ (0x47) Expected COMMA in List, got: 'G' (\u0047)
~~~


### 0x48 in parameterised list key

Input:
~~~
foo; aHa=1
~~~

Expects Parse Error
~~~
>>foo; aHa=1<<
  ------^ (0x48) Expected COMMA in List, got: 'H' (\u0048)
~~~


### 0x49 in parameterised list key

Input:
~~~
foo; aIa=1
~~~

Expects Parse Error
~~~
>>foo; aIa=1<<
  ------^ (0x49) Expected COMMA in List, got: 'I' (\u0049)
~~~


### 0x4a in parameterised list key

Input:
~~~
foo; aJa=1
~~~

Expects Parse Error
~~~
>>foo; aJa=1<<
  ------^ (0x4a) Expected COMMA in List, got: 'J' (\u004a)
~~~


### 0x4b in parameterised list key

Input:
~~~
foo; aKa=1
~~~

Expects Parse Error
~~~
>>foo; aKa=1<<
  ------^ (0x4b) Expected COMMA in List, got: 'K' (\u004b)
~~~


### 0x4c in parameterised list key

Input:
~~~
foo; aLa=1
~~~

Expects Parse Error
~~~
>>foo; aLa=1<<
  ------^ (0x4c) Expected COMMA in List, got: 'L' (\u004c)
~~~


### 0x4d in parameterised list key

Input:
~~~
foo; aMa=1
~~~

Expects Parse Error
~~~
>>foo; aMa=1<<
  ------^ (0x4d) Expected COMMA in List, got: 'M' (\u004d)
~~~


### 0x4e in parameterised list key

Input:
~~~
foo; aNa=1
~~~

Expects Parse Error
~~~
>>foo; aNa=1<<
  ------^ (0x4e) Expected COMMA in List, got: 'N' (\u004e)
~~~


### 0x4f in parameterised list key

Input:
~~~
foo; aOa=1
~~~

Expects Parse Error
~~~
>>foo; aOa=1<<
  ------^ (0x4f) Expected COMMA in List, got: 'O' (\u004f)
~~~


### 0x50 in parameterised list key

Input:
~~~
foo; aPa=1
~~~

Expects Parse Error
~~~
>>foo; aPa=1<<
  ------^ (0x50) Expected COMMA in List, got: 'P' (\u0050)
~~~


### 0x51 in parameterised list key

Input:
~~~
foo; aQa=1
~~~

Expects Parse Error
~~~
>>foo; aQa=1<<
  ------^ (0x51) Expected COMMA in List, got: 'Q' (\u0051)
~~~


### 0x52 in parameterised list key

Input:
~~~
foo; aRa=1
~~~

Expects Parse Error
~~~
>>foo; aRa=1<<
  ------^ (0x52) Expected COMMA in List, got: 'R' (\u0052)
~~~


### 0x53 in parameterised list key

Input:
~~~
foo; aSa=1
~~~

Expects Parse Error
~~~
>>foo; aSa=1<<
  ------^ (0x53) Expected COMMA in List, got: 'S' (\u0053)
~~~


### 0x54 in parameterised list key

Input:
~~~
foo; aTa=1
~~~

Expects Parse Error
~~~
>>foo; aTa=1<<
  ------^ (0x54) Expected COMMA in List, got: 'T' (\u0054)
~~~


### 0x55 in parameterised list key

Input:
~~~
foo; aUa=1
~~~

Expects Parse Error
~~~
>>foo; aUa=1<<
  ------^ (0x55) Expected COMMA in List, got: 'U' (\u0055)
~~~


### 0x56 in parameterised list key

Input:
~~~
foo; aVa=1
~~~

Expects Parse Error
~~~
>>foo; aVa=1<<
  ------^ (0x56) Expected COMMA in List, got: 'V' (\u0056)
~~~


### 0x57 in parameterised list key

Input:
~~~
foo; aWa=1
~~~

Expects Parse Error
~~~
>>foo; aWa=1<<
  ------^ (0x57) Expected COMMA in List, got: 'W' (\u0057)
~~~


### 0x58 in parameterised list key

Input:
~~~
foo; aXa=1
~~~

Expects Parse Error
~~~
>>foo; aXa=1<<
  ------^ (0x58) Expected COMMA in List, got: 'X' (\u0058)
~~~


### 0x59 in parameterised list key

Input:
~~~
foo; aYa=1
~~~

Expects Parse Error
~~~
>>foo; aYa=1<<
  ------^ (0x59) Expected COMMA in List, got: 'Y' (\u0059)
~~~


### 0x5a in parameterised list key

Input:
~~~
foo; aZa=1
~~~

Expects Parse Error
~~~
>>foo; aZa=1<<
  ------^ (0x5a) Expected COMMA in List, got: 'Z' (\u005a)
~~~


### 0x5b in parameterised list key

Input:
~~~
foo; a[a=1
~~~

Expects Parse Error
~~~
>>foo; a[a=1<<
  ------^ (0x5b) Expected COMMA in List, got: '[' (\u005b)
~~~


### 0x5c in parameterised list key

Input:
~~~
foo; a\a=1
~~~

Expects Parse Error
~~~
>>foo; a\a=1<<
  ------^ (0x5c) Expected COMMA in List, got: '\' (\u005c)
~~~


### 0x5d in parameterised list key

Input:
~~~
foo; a]a=1
~~~

Expects Parse Error
~~~
>>foo; a]a=1<<
  ------^ (0x5d) Expected COMMA in List, got: ']' (\u005d)
~~~


### 0x5e in parameterised list key

Input:
~~~
foo; a^a=1
~~~

Expects Parse Error
~~~
>>foo; a^a=1<<
  ------^ (0x5e) Expected COMMA in List, got: '^' (\u005e)
~~~


### 0x5f in parameterised list key

Input:
~~~
foo; a_a=1
~~~

Result:
~~~
foo;a_a=1
~~~

### 0x60 in parameterised list key

Input:
~~~
foo; a`a=1
~~~

Expects Parse Error
~~~
>>foo; a`a=1<<
  ------^ (0x60) Expected COMMA in List, got: '`' (\u0060)
~~~


### 0x61 in parameterised list key

Input:
~~~
foo; aaa=1
~~~

Result:
~~~
foo;aaa=1
~~~

### 0x62 in parameterised list key

Input:
~~~
foo; aba=1
~~~

Result:
~~~
foo;aba=1
~~~

### 0x63 in parameterised list key

Input:
~~~
foo; aca=1
~~~

Result:
~~~
foo;aca=1
~~~

### 0x64 in parameterised list key

Input:
~~~
foo; ada=1
~~~

Result:
~~~
foo;ada=1
~~~

### 0x65 in parameterised list key

Input:
~~~
foo; aea=1
~~~

Result:
~~~
foo;aea=1
~~~

### 0x66 in parameterised list key

Input:
~~~
foo; afa=1
~~~

Result:
~~~
foo;afa=1
~~~

### 0x67 in parameterised list key

Input:
~~~
foo; aga=1
~~~

Result:
~~~
foo;aga=1
~~~

### 0x68 in parameterised list key

Input:
~~~
foo; aha=1
~~~

Result:
~~~
foo;aha=1
~~~

### 0x69 in parameterised list key

Input:
~~~
foo; aia=1
~~~

Result:
~~~
foo;aia=1
~~~

### 0x6a in parameterised list key

Input:
~~~
foo; aja=1
~~~

Result:
~~~
foo;aja=1
~~~

### 0x6b in parameterised list key

Input:
~~~
foo; aka=1
~~~

Result:
~~~
foo;aka=1
~~~

### 0x6c in parameterised list key

Input:
~~~
foo; ala=1
~~~

Result:
~~~
foo;ala=1
~~~

### 0x6d in parameterised list key

Input:
~~~
foo; ama=1
~~~

Result:
~~~
foo;ama=1
~~~

### 0x6e in parameterised list key

Input:
~~~
foo; ana=1
~~~

Result:
~~~
foo;ana=1
~~~

### 0x6f in parameterised list key

Input:
~~~
foo; aoa=1
~~~

Result:
~~~
foo;aoa=1
~~~

### 0x70 in parameterised list key

Input:
~~~
foo; apa=1
~~~

Result:
~~~
foo;apa=1
~~~

### 0x71 in parameterised list key

Input:
~~~
foo; aqa=1
~~~

Result:
~~~
foo;aqa=1
~~~

### 0x72 in parameterised list key

Input:
~~~
foo; ara=1
~~~

Result:
~~~
foo;ara=1
~~~

### 0x73 in parameterised list key

Input:
~~~
foo; asa=1
~~~

Result:
~~~
foo;asa=1
~~~

### 0x74 in parameterised list key

Input:
~~~
foo; ata=1
~~~

Result:
~~~
foo;ata=1
~~~

### 0x75 in parameterised list key

Input:
~~~
foo; aua=1
~~~

Result:
~~~
foo;aua=1
~~~

### 0x76 in parameterised list key

Input:
~~~
foo; ava=1
~~~

Result:
~~~
foo;ava=1
~~~

### 0x77 in parameterised list key

Input:
~~~
foo; awa=1
~~~

Result:
~~~
foo;awa=1
~~~

### 0x78 in parameterised list key

Input:
~~~
foo; axa=1
~~~

Result:
~~~
foo;axa=1
~~~

### 0x79 in parameterised list key

Input:
~~~
foo; aya=1
~~~

Result:
~~~
foo;aya=1
~~~

### 0x7a in parameterised list key

Input:
~~~
foo; aza=1
~~~

Result:
~~~
foo;aza=1
~~~

### 0x7b in parameterised list key

Input:
~~~
foo; a{a=1
~~~

Expects Parse Error
~~~
>>foo; a{a=1<<
  ------^ (0x7b) Expected COMMA in List, got: '{' (\u007b)
~~~


### 0x7c in parameterised list key

Input:
~~~
foo; a|a=1
~~~

Expects Parse Error
~~~
>>foo; a|a=1<<
  ------^ (0x7c) Expected COMMA in List, got: '|' (\u007c)
~~~


### 0x7d in parameterised list key

Input:
~~~
foo; a}a=1
~~~

Expects Parse Error
~~~
>>foo; a}a=1<<
  ------^ (0x7d) Expected COMMA in List, got: '}' (\u007d)
~~~


### 0x7e in parameterised list key

Input:
~~~
foo; a~a=1
~~~

Expects Parse Error
~~~
>>foo; a~a=1<<
  ------^ (0x7e) Expected COMMA in List, got: '~' (\u007e)
~~~


### 0x7f in parameterised list key

Input:
~~~
foo; aa=1
~~~

Expects Parse Error
~~~
>>foo; aa=1<<
  ------^ (0x7f) Expected COMMA in List, got: '' (\u007f)
~~~


### 0x00 starting a parameterised list key

Input:
~~~
foo;  a=1
~~~

Expects Parse Error
~~~
>>foo;  a=1<<
  -----^ (0x00) Key must start with LCALPHA or '*': ' ' (\u0000)
~~~


### 0x01 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x01) Key must start with LCALPHA or '*': '' (\u0001)
~~~


### 0x02 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x02) Key must start with LCALPHA or '*': '' (\u0002)
~~~


### 0x03 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x03) Key must start with LCALPHA or '*': '' (\u0003)
~~~


### 0x04 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x04) Key must start with LCALPHA or '*': '' (\u0004)
~~~


### 0x05 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x05) Key must start with LCALPHA or '*': '' (\u0005)
~~~


### 0x06 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x06) Key must start with LCALPHA or '*': '' (\u0006)
~~~


### 0x07 starting a parameterised list key

Input:
~~~
foo;  a=1
~~~

Expects Parse Error
~~~
>>foo;  a=1<<
  -----^ (0x07) Key must start with LCALPHA or '*': ' ' (\u0007)
~~~


### 0x08 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x08) Key must start with LCALPHA or '*': '' (\u0008)
~~~


### 0x09 starting a parameterised list key

Input:
~~~
foo; 	a=1
~~~

Expects Parse Error
~~~
>>foo; 	a=1<<
  -----^ (0x09) Key must start with LCALPHA or '*': HTAB (\u0009)
~~~


### 0x0a starting a parameterised list key

Input:
~~~
foo; 
a=1
~~~

Expects Parse Error
~~~
>>foo; 
a=1<<
  -----^ (0x0a) Key must start with LCALPHA or '*': '
' (\u000a)
~~~


### 0x0b starting a parameterised list key

Input:
~~~
foo; 
a=1
~~~

Expects Parse Error
~~~
>>foo; 
a=1<<
  -----^ (0x0b) Key must start with LCALPHA or '*': '
' (\u000b)
~~~


### 0x0c starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x0c) Key must start with LCALPHA or '*': '' (\u000c)
~~~


### 0x0d starting a parameterised list key

Input:
~~~
foo; 
a=1
~~~

Expects Parse Error
~~~
>>foo; 
a=1<<
  -----^ (0x0d) Key must start with LCALPHA or '*': '
' (\u000d)
~~~


### 0x0e starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x0e) Key must start with LCALPHA or '*': '' (\u000e)
~~~


### 0x0f starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x0f) Key must start with LCALPHA or '*': '' (\u000f)
~~~


### 0x10 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x10) Key must start with LCALPHA or '*': '' (\u0010)
~~~


### 0x11 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x11) Key must start with LCALPHA or '*': '' (\u0011)
~~~


### 0x12 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x12) Key must start with LCALPHA or '*': '' (\u0012)
~~~


### 0x13 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x13) Key must start with LCALPHA or '*': '' (\u0013)
~~~


### 0x14 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x14) Key must start with LCALPHA or '*': '' (\u0014)
~~~


### 0x15 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x15) Key must start with LCALPHA or '*': '' (\u0015)
~~~


### 0x16 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x16) Key must start with LCALPHA or '*': '' (\u0016)
~~~


### 0x17 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x17) Key must start with LCALPHA or '*': '' (\u0017)
~~~


### 0x18 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x18) Key must start with LCALPHA or '*': '' (\u0018)
~~~


### 0x19 starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x19) Key must start with LCALPHA or '*': '' (\u0019)
~~~


### 0x1a starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x1a) Key must start with LCALPHA or '*': '' (\u001a)
~~~


### 0x1b starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x1b) Key must start with LCALPHA or '*': '' (\u001b)
~~~


### 0x1c starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x1c) Key must start with LCALPHA or '*': '' (\u001c)
~~~


### 0x1d starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x1d) Key must start with LCALPHA or '*': '' (\u001d)
~~~


### 0x1e starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x1e) Key must start with LCALPHA or '*': '' (\u001e)
~~~


### 0x1f starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x1f) Key must start with LCALPHA or '*': '' (\u001f)
~~~


### 0x20 starting a parameterised list key

Input:
~~~
foo;  a=1
~~~

Result:
~~~
foo;a=1
~~~

### 0x21 starting a parameterised list key

Input:
~~~
foo; !a=1
~~~

Expects Parse Error
~~~
>>foo; !a=1<<
  -----^ (0x21) Key must start with LCALPHA or '*': '!' (\u0021)
~~~


### 0x22 starting a parameterised list key

Input:
~~~
foo; "a=1
~~~

Expects Parse Error
~~~
>>foo; "a=1<<
  -----^ (0x22) Key must start with LCALPHA or '*': '"' (\u0022)
~~~


### 0x23 starting a parameterised list key

Input:
~~~
foo; #a=1
~~~

Expects Parse Error
~~~
>>foo; #a=1<<
  -----^ (0x23) Key must start with LCALPHA or '*': '#' (\u0023)
~~~


### 0x24 starting a parameterised list key

Input:
~~~
foo; $a=1
~~~

Expects Parse Error
~~~
>>foo; $a=1<<
  -----^ (0x24) Key must start with LCALPHA or '*': '$' (\u0024)
~~~


### 0x25 starting a parameterised list key

Input:
~~~
foo; %a=1
~~~

Expects Parse Error
~~~
>>foo; %a=1<<
  -----^ (0x25) Key must start with LCALPHA or '*': '%' (\u0025)
~~~


### 0x26 starting a parameterised list key

Input:
~~~
foo; &a=1
~~~

Expects Parse Error
~~~
>>foo; &a=1<<
  -----^ (0x26) Key must start with LCALPHA or '*': '&' (\u0026)
~~~


### 0x27 starting a parameterised list key

Input:
~~~
foo; 'a=1
~~~

Expects Parse Error
~~~
>>foo; 'a=1<<
  -----^ (0x27) Key must start with LCALPHA or '*': ''' (\u0027)
~~~


### 0x28 starting a parameterised list key

Input:
~~~
foo; (a=1
~~~

Expects Parse Error
~~~
>>foo; (a=1<<
  -----^ (0x28) Key must start with LCALPHA or '*': '(' (\u0028)
~~~


### 0x29 starting a parameterised list key

Input:
~~~
foo; )a=1
~~~

Expects Parse Error
~~~
>>foo; )a=1<<
  -----^ (0x29) Key must start with LCALPHA or '*': ')' (\u0029)
~~~


### 0x2a starting a parameterised list key

Input:
~~~
foo; *a=1
~~~

Result:
~~~
foo;*a=1
~~~

### 0x2b starting a parameterised list key

Input:
~~~
foo; +a=1
~~~

Expects Parse Error
~~~
>>foo; +a=1<<
  -----^ (0x2b) Key must start with LCALPHA or '*': '+' (\u002b)
~~~


### 0x2c starting a parameterised list key

Input:
~~~
foo; ,a=1
~~~

Expects Parse Error
~~~
>>foo; ,a=1<<
  -----^ (0x2c) Key must start with LCALPHA or '*': ',' (\u002c)
~~~


### 0x2d starting a parameterised list key

Input:
~~~
foo; -a=1
~~~

Expects Parse Error
~~~
>>foo; -a=1<<
  -----^ (0x2d) Key must start with LCALPHA or '*': '-' (\u002d)
~~~


### 0x2e starting a parameterised list key

Input:
~~~
foo; .a=1
~~~

Expects Parse Error
~~~
>>foo; .a=1<<
  -----^ (0x2e) Key must start with LCALPHA or '*': '.' (\u002e)
~~~


### 0x2f starting a parameterised list key

Input:
~~~
foo; /a=1
~~~

Expects Parse Error
~~~
>>foo; /a=1<<
  -----^ (0x2f) Key must start with LCALPHA or '*': '/' (\u002f)
~~~


### 0x30 starting a parameterised list key

Input:
~~~
foo; 0a=1
~~~

Expects Parse Error
~~~
>>foo; 0a=1<<
  -----^ (0x30) Key must start with LCALPHA or '*': '0' (\u0030)
~~~


### 0x31 starting a parameterised list key

Input:
~~~
foo; 1a=1
~~~

Expects Parse Error
~~~
>>foo; 1a=1<<
  -----^ (0x31) Key must start with LCALPHA or '*': '1' (\u0031)
~~~


### 0x32 starting a parameterised list key

Input:
~~~
foo; 2a=1
~~~

Expects Parse Error
~~~
>>foo; 2a=1<<
  -----^ (0x32) Key must start with LCALPHA or '*': '2' (\u0032)
~~~


### 0x33 starting a parameterised list key

Input:
~~~
foo; 3a=1
~~~

Expects Parse Error
~~~
>>foo; 3a=1<<
  -----^ (0x33) Key must start with LCALPHA or '*': '3' (\u0033)
~~~


### 0x34 starting a parameterised list key

Input:
~~~
foo; 4a=1
~~~

Expects Parse Error
~~~
>>foo; 4a=1<<
  -----^ (0x34) Key must start with LCALPHA or '*': '4' (\u0034)
~~~


### 0x35 starting a parameterised list key

Input:
~~~
foo; 5a=1
~~~

Expects Parse Error
~~~
>>foo; 5a=1<<
  -----^ (0x35) Key must start with LCALPHA or '*': '5' (\u0035)
~~~


### 0x36 starting a parameterised list key

Input:
~~~
foo; 6a=1
~~~

Expects Parse Error
~~~
>>foo; 6a=1<<
  -----^ (0x36) Key must start with LCALPHA or '*': '6' (\u0036)
~~~


### 0x37 starting a parameterised list key

Input:
~~~
foo; 7a=1
~~~

Expects Parse Error
~~~
>>foo; 7a=1<<
  -----^ (0x37) Key must start with LCALPHA or '*': '7' (\u0037)
~~~


### 0x38 starting a parameterised list key

Input:
~~~
foo; 8a=1
~~~

Expects Parse Error
~~~
>>foo; 8a=1<<
  -----^ (0x38) Key must start with LCALPHA or '*': '8' (\u0038)
~~~


### 0x39 starting a parameterised list key

Input:
~~~
foo; 9a=1
~~~

Expects Parse Error
~~~
>>foo; 9a=1<<
  -----^ (0x39) Key must start with LCALPHA or '*': '9' (\u0039)
~~~


### 0x3a starting a parameterised list key

Input:
~~~
foo; :a=1
~~~

Expects Parse Error
~~~
>>foo; :a=1<<
  -----^ (0x3a) Key must start with LCALPHA or '*': ':' (\u003a)
~~~


### 0x3b starting a parameterised list key

Input:
~~~
foo; ;a=1
~~~

Expects Parse Error
~~~
>>foo; ;a=1<<
  -----^ (0x3b) Key must start with LCALPHA or '*': ';' (\u003b)
~~~


### 0x3c starting a parameterised list key

Input:
~~~
foo; <a=1
~~~

Expects Parse Error
~~~
>>foo; <a=1<<
  -----^ (0x3c) Key must start with LCALPHA or '*': '<' (\u003c)
~~~


### 0x3d starting a parameterised list key

Input:
~~~
foo; =a=1
~~~

Expects Parse Error
~~~
>>foo; =a=1<<
  -----^ (0x3d) Key must start with LCALPHA or '*': '=' (\u003d)
~~~


### 0x3e starting a parameterised list key

Input:
~~~
foo; >a=1
~~~

Expects Parse Error
~~~
>>foo; >a=1<<
  -----^ (0x3e) Key must start with LCALPHA or '*': '>' (\u003e)
~~~


### 0x3f starting a parameterised list key

Input:
~~~
foo; ?a=1
~~~

Expects Parse Error
~~~
>>foo; ?a=1<<
  -----^ (0x3f) Key must start with LCALPHA or '*': '?' (\u003f)
~~~


### 0x40 starting a parameterised list key

Input:
~~~
foo; @a=1
~~~

Expects Parse Error
~~~
>>foo; @a=1<<
  -----^ (0x40) Key must start with LCALPHA or '*': '@' (\u0040)
~~~


### 0x41 starting a parameterised list key

Input:
~~~
foo; Aa=1
~~~

Expects Parse Error
~~~
>>foo; Aa=1<<
  -----^ (0x41) Key must start with LCALPHA or '*': 'A' (\u0041)
~~~


### 0x42 starting a parameterised list key

Input:
~~~
foo; Ba=1
~~~

Expects Parse Error
~~~
>>foo; Ba=1<<
  -----^ (0x42) Key must start with LCALPHA or '*': 'B' (\u0042)
~~~


### 0x43 starting a parameterised list key

Input:
~~~
foo; Ca=1
~~~

Expects Parse Error
~~~
>>foo; Ca=1<<
  -----^ (0x43) Key must start with LCALPHA or '*': 'C' (\u0043)
~~~


### 0x44 starting a parameterised list key

Input:
~~~
foo; Da=1
~~~

Expects Parse Error
~~~
>>foo; Da=1<<
  -----^ (0x44) Key must start with LCALPHA or '*': 'D' (\u0044)
~~~


### 0x45 starting a parameterised list key

Input:
~~~
foo; Ea=1
~~~

Expects Parse Error
~~~
>>foo; Ea=1<<
  -----^ (0x45) Key must start with LCALPHA or '*': 'E' (\u0045)
~~~


### 0x46 starting a parameterised list key

Input:
~~~
foo; Fa=1
~~~

Expects Parse Error
~~~
>>foo; Fa=1<<
  -----^ (0x46) Key must start with LCALPHA or '*': 'F' (\u0046)
~~~


### 0x47 starting a parameterised list key

Input:
~~~
foo; Ga=1
~~~

Expects Parse Error
~~~
>>foo; Ga=1<<
  -----^ (0x47) Key must start with LCALPHA or '*': 'G' (\u0047)
~~~


### 0x48 starting a parameterised list key

Input:
~~~
foo; Ha=1
~~~

Expects Parse Error
~~~
>>foo; Ha=1<<
  -----^ (0x48) Key must start with LCALPHA or '*': 'H' (\u0048)
~~~


### 0x49 starting a parameterised list key

Input:
~~~
foo; Ia=1
~~~

Expects Parse Error
~~~
>>foo; Ia=1<<
  -----^ (0x49) Key must start with LCALPHA or '*': 'I' (\u0049)
~~~


### 0x4a starting a parameterised list key

Input:
~~~
foo; Ja=1
~~~

Expects Parse Error
~~~
>>foo; Ja=1<<
  -----^ (0x4a) Key must start with LCALPHA or '*': 'J' (\u004a)
~~~


### 0x4b starting a parameterised list key

Input:
~~~
foo; Ka=1
~~~

Expects Parse Error
~~~
>>foo; Ka=1<<
  -----^ (0x4b) Key must start with LCALPHA or '*': 'K' (\u004b)
~~~


### 0x4c starting a parameterised list key

Input:
~~~
foo; La=1
~~~

Expects Parse Error
~~~
>>foo; La=1<<
  -----^ (0x4c) Key must start with LCALPHA or '*': 'L' (\u004c)
~~~


### 0x4d starting a parameterised list key

Input:
~~~
foo; Ma=1
~~~

Expects Parse Error
~~~
>>foo; Ma=1<<
  -----^ (0x4d) Key must start with LCALPHA or '*': 'M' (\u004d)
~~~


### 0x4e starting a parameterised list key

Input:
~~~
foo; Na=1
~~~

Expects Parse Error
~~~
>>foo; Na=1<<
  -----^ (0x4e) Key must start with LCALPHA or '*': 'N' (\u004e)
~~~


### 0x4f starting a parameterised list key

Input:
~~~
foo; Oa=1
~~~

Expects Parse Error
~~~
>>foo; Oa=1<<
  -----^ (0x4f) Key must start with LCALPHA or '*': 'O' (\u004f)
~~~


### 0x50 starting a parameterised list key

Input:
~~~
foo; Pa=1
~~~

Expects Parse Error
~~~
>>foo; Pa=1<<
  -----^ (0x50) Key must start with LCALPHA or '*': 'P' (\u0050)
~~~


### 0x51 starting a parameterised list key

Input:
~~~
foo; Qa=1
~~~

Expects Parse Error
~~~
>>foo; Qa=1<<
  -----^ (0x51) Key must start with LCALPHA or '*': 'Q' (\u0051)
~~~


### 0x52 starting a parameterised list key

Input:
~~~
foo; Ra=1
~~~

Expects Parse Error
~~~
>>foo; Ra=1<<
  -----^ (0x52) Key must start with LCALPHA or '*': 'R' (\u0052)
~~~


### 0x53 starting a parameterised list key

Input:
~~~
foo; Sa=1
~~~

Expects Parse Error
~~~
>>foo; Sa=1<<
  -----^ (0x53) Key must start with LCALPHA or '*': 'S' (\u0053)
~~~


### 0x54 starting a parameterised list key

Input:
~~~
foo; Ta=1
~~~

Expects Parse Error
~~~
>>foo; Ta=1<<
  -----^ (0x54) Key must start with LCALPHA or '*': 'T' (\u0054)
~~~


### 0x55 starting a parameterised list key

Input:
~~~
foo; Ua=1
~~~

Expects Parse Error
~~~
>>foo; Ua=1<<
  -----^ (0x55) Key must start with LCALPHA or '*': 'U' (\u0055)
~~~


### 0x56 starting a parameterised list key

Input:
~~~
foo; Va=1
~~~

Expects Parse Error
~~~
>>foo; Va=1<<
  -----^ (0x56) Key must start with LCALPHA or '*': 'V' (\u0056)
~~~


### 0x57 starting a parameterised list key

Input:
~~~
foo; Wa=1
~~~

Expects Parse Error
~~~
>>foo; Wa=1<<
  -----^ (0x57) Key must start with LCALPHA or '*': 'W' (\u0057)
~~~


### 0x58 starting a parameterised list key

Input:
~~~
foo; Xa=1
~~~

Expects Parse Error
~~~
>>foo; Xa=1<<
  -----^ (0x58) Key must start with LCALPHA or '*': 'X' (\u0058)
~~~


### 0x59 starting a parameterised list key

Input:
~~~
foo; Ya=1
~~~

Expects Parse Error
~~~
>>foo; Ya=1<<
  -----^ (0x59) Key must start with LCALPHA or '*': 'Y' (\u0059)
~~~


### 0x5a starting a parameterised list key

Input:
~~~
foo; Za=1
~~~

Expects Parse Error
~~~
>>foo; Za=1<<
  -----^ (0x5a) Key must start with LCALPHA or '*': 'Z' (\u005a)
~~~


### 0x5b starting a parameterised list key

Input:
~~~
foo; [a=1
~~~

Expects Parse Error
~~~
>>foo; [a=1<<
  -----^ (0x5b) Key must start with LCALPHA or '*': '[' (\u005b)
~~~


### 0x5c starting a parameterised list key

Input:
~~~
foo; \a=1
~~~

Expects Parse Error
~~~
>>foo; \a=1<<
  -----^ (0x5c) Key must start with LCALPHA or '*': '\' (\u005c)
~~~


### 0x5d starting a parameterised list key

Input:
~~~
foo; ]a=1
~~~

Expects Parse Error
~~~
>>foo; ]a=1<<
  -----^ (0x5d) Key must start with LCALPHA or '*': ']' (\u005d)
~~~


### 0x5e starting a parameterised list key

Input:
~~~
foo; ^a=1
~~~

Expects Parse Error
~~~
>>foo; ^a=1<<
  -----^ (0x5e) Key must start with LCALPHA or '*': '^' (\u005e)
~~~


### 0x5f starting a parameterised list key

Input:
~~~
foo; _a=1
~~~

Expects Parse Error
~~~
>>foo; _a=1<<
  -----^ (0x5f) Key must start with LCALPHA or '*': '_' (\u005f)
~~~


### 0x60 starting a parameterised list key

Input:
~~~
foo; `a=1
~~~

Expects Parse Error
~~~
>>foo; `a=1<<
  -----^ (0x60) Key must start with LCALPHA or '*': '`' (\u0060)
~~~


### 0x61 starting a parameterised list key

Input:
~~~
foo; aa=1
~~~

Result:
~~~
foo;aa=1
~~~

### 0x62 starting a parameterised list key

Input:
~~~
foo; ba=1
~~~

Result:
~~~
foo;ba=1
~~~

### 0x63 starting a parameterised list key

Input:
~~~
foo; ca=1
~~~

Result:
~~~
foo;ca=1
~~~

### 0x64 starting a parameterised list key

Input:
~~~
foo; da=1
~~~

Result:
~~~
foo;da=1
~~~

### 0x65 starting a parameterised list key

Input:
~~~
foo; ea=1
~~~

Result:
~~~
foo;ea=1
~~~

### 0x66 starting a parameterised list key

Input:
~~~
foo; fa=1
~~~

Result:
~~~
foo;fa=1
~~~

### 0x67 starting a parameterised list key

Input:
~~~
foo; ga=1
~~~

Result:
~~~
foo;ga=1
~~~

### 0x68 starting a parameterised list key

Input:
~~~
foo; ha=1
~~~

Result:
~~~
foo;ha=1
~~~

### 0x69 starting a parameterised list key

Input:
~~~
foo; ia=1
~~~

Result:
~~~
foo;ia=1
~~~

### 0x6a starting a parameterised list key

Input:
~~~
foo; ja=1
~~~

Result:
~~~
foo;ja=1
~~~

### 0x6b starting a parameterised list key

Input:
~~~
foo; ka=1
~~~

Result:
~~~
foo;ka=1
~~~

### 0x6c starting a parameterised list key

Input:
~~~
foo; la=1
~~~

Result:
~~~
foo;la=1
~~~

### 0x6d starting a parameterised list key

Input:
~~~
foo; ma=1
~~~

Result:
~~~
foo;ma=1
~~~

### 0x6e starting a parameterised list key

Input:
~~~
foo; na=1
~~~

Result:
~~~
foo;na=1
~~~

### 0x6f starting a parameterised list key

Input:
~~~
foo; oa=1
~~~

Result:
~~~
foo;oa=1
~~~

### 0x70 starting a parameterised list key

Input:
~~~
foo; pa=1
~~~

Result:
~~~
foo;pa=1
~~~

### 0x71 starting a parameterised list key

Input:
~~~
foo; qa=1
~~~

Result:
~~~
foo;qa=1
~~~

### 0x72 starting a parameterised list key

Input:
~~~
foo; ra=1
~~~

Result:
~~~
foo;ra=1
~~~

### 0x73 starting a parameterised list key

Input:
~~~
foo; sa=1
~~~

Result:
~~~
foo;sa=1
~~~

### 0x74 starting a parameterised list key

Input:
~~~
foo; ta=1
~~~

Result:
~~~
foo;ta=1
~~~

### 0x75 starting a parameterised list key

Input:
~~~
foo; ua=1
~~~

Result:
~~~
foo;ua=1
~~~

### 0x76 starting a parameterised list key

Input:
~~~
foo; va=1
~~~

Result:
~~~
foo;va=1
~~~

### 0x77 starting a parameterised list key

Input:
~~~
foo; wa=1
~~~

Result:
~~~
foo;wa=1
~~~

### 0x78 starting a parameterised list key

Input:
~~~
foo; xa=1
~~~

Result:
~~~
foo;xa=1
~~~

### 0x79 starting a parameterised list key

Input:
~~~
foo; ya=1
~~~

Result:
~~~
foo;ya=1
~~~

### 0x7a starting a parameterised list key

Input:
~~~
foo; za=1
~~~

Result:
~~~
foo;za=1
~~~

### 0x7b starting a parameterised list key

Input:
~~~
foo; {a=1
~~~

Expects Parse Error
~~~
>>foo; {a=1<<
  -----^ (0x7b) Key must start with LCALPHA or '*': '{' (\u007b)
~~~


### 0x7c starting a parameterised list key

Input:
~~~
foo; |a=1
~~~

Expects Parse Error
~~~
>>foo; |a=1<<
  -----^ (0x7c) Key must start with LCALPHA or '*': '|' (\u007c)
~~~


### 0x7d starting a parameterised list key

Input:
~~~
foo; }a=1
~~~

Expects Parse Error
~~~
>>foo; }a=1<<
  -----^ (0x7d) Key must start with LCALPHA or '*': '}' (\u007d)
~~~


### 0x7e starting a parameterised list key

Input:
~~~
foo; ~a=1
~~~

Expects Parse Error
~~~
>>foo; ~a=1<<
  -----^ (0x7e) Key must start with LCALPHA or '*': '~' (\u007e)
~~~


### 0x7f starting a parameterised list key

Input:
~~~
foo; a=1
~~~

Expects Parse Error
~~~
>>foo; a=1<<
  -----^ (0x7f) Key must start with LCALPHA or '*': '' (\u007f)
~~~



## large-generated


### large dictionary

Input:
~~~
a0=1, a1=1, a2=1, a3=1, a4=1, a5=1, a6=1, a7=1, a8=1, a9=1, a10=1, a11=1, a12=1, a13=1, a14=1, a15=1, a16=1, a17=1, a18=1, a19=1, a20=1, a21=1, a22=1, a23=1, a24=1, a25=1, a26=1, a27=1, a28=1, a29=1, a30=1, a31=1, a32=1, a33=1, a34=1, a35=1, a36=1, a37=1, a38=1, a39=1, a40=1, a41=1, a42=1, a43=1, a44=1, a45=1, a46=1, a47=1, a48=1, a49=1, a50=1, a51=1, a52=1, a53=1, a54=1, a55=1, a56=1, a57=1, a58=1, a59=1, a60=1, a61=1, a62=1, a63=1, a64=1, a65=1, a66=1, a67=1, a68=1, a69=1, a70=1, a71=1, a72=1, a73=1, a74=1, a75=1, a76=1, a77=1, a78=1, a79=1, a80=1, a81=1, a82=1, a83=1, a84=1, a85=1, a86=1, a87=1, a88=1, a89=1, a90=1, a91=1, a92=1, a93=1, a94=1, a95=1, a96=1, a97=1, a98=1, a99=1, a100=1, a101=1, a102=1, a103=1, a104=1, a105=1, a106=1, a107=1, a108=1, a109=1, a110=1, a111=1, a112=1, a113=1, a114=1, a115=1, a116=1, a117=1, a118=1, a119=1, a120=1, a121=1, a122=1, a123=1, a124=1, a125=1, a126=1, a127=1, a128=1, a129=1, a130=1, a131=1, a132=1, a133=1, a134=1, a135=1, a136=1, a137=1, a138=1, a139=1, a140=1, a141=1, a142=1, a143=1, a144=1, a145=1, a146=1, a147=1, a148=1, a149=1, a150=1, a151=1, a152=1, a153=1, a154=1, a155=1, a156=1, a157=1, a158=1, a159=1, a160=1, a161=1, a162=1, a163=1, a164=1, a165=1, a166=1, a167=1, a168=1, a169=1, a170=1, a171=1, a172=1, a173=1, a174=1, a175=1, a176=1, a177=1, a178=1, a179=1, a180=1, a181=1, a182=1, a183=1, a184=1, a185=1, a186=1, a187=1, a188=1, a189=1, a190=1, a191=1, a192=1, a193=1, a194=1, a195=1, a196=1, a197=1, a198=1, a199=1, a200=1, a201=1, a202=1, a203=1, a204=1, a205=1, a206=1, a207=1, a208=1, a209=1, a210=1, a211=1, a212=1, a213=1, a214=1, a215=1, a216=1, a217=1, a218=1, a219=1, a220=1, a221=1, a222=1, a223=1, a224=1, a225=1, a226=1, a227=1, a228=1, a229=1, a230=1, a231=1, a232=1, a233=1, a234=1, a235=1, a236=1, a237=1, a238=1, a239=1, a240=1, a241=1, a242=1, a243=1, a244=1, a245=1, a246=1, a247=1, a248=1, a249=1, a250=1, a251=1, a252=1, a253=1, a254=1, a255=1, a256=1, a257=1, a258=1, a259=1, a260=1, a261=1, a262=1, a263=1, a264=1, a265=1, a266=1, a267=1, a268=1, a269=1, a270=1, a271=1, a272=1, a273=1, a274=1, a275=1, a276=1, a277=1, a278=1, a279=1, a280=1, a281=1, a282=1, a283=1, a284=1, a285=1, a286=1, a287=1, a288=1, a289=1, a290=1, a291=1, a292=1, a293=1, a294=1, a295=1, a296=1, a297=1, a298=1, a299=1, a300=1, a301=1, a302=1, a303=1, a304=1, a305=1, a306=1, a307=1, a308=1, a309=1, a310=1, a311=1, a312=1, a313=1, a314=1, a315=1, a316=1, a317=1, a318=1, a319=1, a320=1, a321=1, a322=1, a323=1, a324=1, a325=1, a326=1, a327=1, a328=1, a329=1, a330=1, a331=1, a332=1, a333=1, a334=1, a335=1, a336=1, a337=1, a338=1, a339=1, a340=1, a341=1, a342=1, a343=1, a344=1, a345=1, a346=1, a347=1, a348=1, a349=1, a350=1, a351=1, a352=1, a353=1, a354=1, a355=1, a356=1, a357=1, a358=1, a359=1, a360=1, a361=1, a362=1, a363=1, a364=1, a365=1, a366=1, a367=1, a368=1, a369=1, a370=1, a371=1, a372=1, a373=1, a374=1, a375=1, a376=1, a377=1, a378=1, a379=1, a380=1, a381=1, a382=1, a383=1, a384=1, a385=1, a386=1, a387=1, a388=1, a389=1, a390=1, a391=1, a392=1, a393=1, a394=1, a395=1, a396=1, a397=1, a398=1, a399=1, a400=1, a401=1, a402=1, a403=1, a404=1, a405=1, a406=1, a407=1, a408=1, a409=1, a410=1, a411=1, a412=1, a413=1, a414=1, a415=1, a416=1, a417=1, a418=1, a419=1, a420=1, a421=1, a422=1, a423=1, a424=1, a425=1, a426=1, a427=1, a428=1, a429=1, a430=1, a431=1, a432=1, a433=1, a434=1, a435=1, a436=1, a437=1, a438=1, a439=1, a440=1, a441=1, a442=1, a443=1, a444=1, a445=1, a446=1, a447=1, a448=1, a449=1, a450=1, a451=1, a452=1, a453=1, a454=1, a455=1, a456=1, a457=1, a458=1, a459=1, a460=1, a461=1, a462=1, a463=1, a464=1, a465=1, a466=1, a467=1, a468=1, a469=1, a470=1, a471=1, a472=1, a473=1, a474=1, a475=1, a476=1, a477=1, a478=1, a479=1, a480=1, a481=1, a482=1, a483=1, a484=1, a485=1, a486=1, a487=1, a488=1, a489=1, a490=1, a491=1, a492=1, a493=1, a494=1, a495=1, a496=1, a497=1, a498=1, a499=1, a500=1, a501=1, a502=1, a503=1, a504=1, a505=1, a506=1, a507=1, a508=1, a509=1, a510=1, a511=1, a512=1, a513=1, a514=1, a515=1, a516=1, a517=1, a518=1, a519=1, a520=1, a521=1, a522=1, a523=1, a524=1, a525=1, a526=1, a527=1, a528=1, a529=1, a530=1, a531=1, a532=1, a533=1, a534=1, a535=1, a536=1, a537=1, a538=1, a539=1, a540=1, a541=1, a542=1, a543=1, a544=1, a545=1, a546=1, a547=1, a548=1, a549=1, a550=1, a551=1, a552=1, a553=1, a554=1, a555=1, a556=1, a557=1, a558=1, a559=1, a560=1, a561=1, a562=1, a563=1, a564=1, a565=1, a566=1, a567=1, a568=1, a569=1, a570=1, a571=1, a572=1, a573=1, a574=1, a575=1, a576=1, a577=1, a578=1, a579=1, a580=1, a581=1, a582=1, a583=1, a584=1, a585=1, a586=1, a587=1, a588=1, a589=1, a590=1, a591=1, a592=1, a593=1, a594=1, a595=1, a596=1, a597=1, a598=1, a599=1, a600=1, a601=1, a602=1, a603=1, a604=1, a605=1, a606=1, a607=1, a608=1, a609=1, a610=1, a611=1, a612=1, a613=1, a614=1, a615=1, a616=1, a617=1, a618=1, a619=1, a620=1, a621=1, a622=1, a623=1, a624=1, a625=1, a626=1, a627=1, a628=1, a629=1, a630=1, a631=1, a632=1, a633=1, a634=1, a635=1, a636=1, a637=1, a638=1, a639=1, a640=1, a641=1, a642=1, a643=1, a644=1, a645=1, a646=1, a647=1, a648=1, a649=1, a650=1, a651=1, a652=1, a653=1, a654=1, a655=1, a656=1, a657=1, a658=1, a659=1, a660=1, a661=1, a662=1, a663=1, a664=1, a665=1, a666=1, a667=1, a668=1, a669=1, a670=1, a671=1, a672=1, a673=1, a674=1, a675=1, a676=1, a677=1, a678=1, a679=1, a680=1, a681=1, a682=1, a683=1, a684=1, a685=1, a686=1, a687=1, a688=1, a689=1, a690=1, a691=1, a692=1, a693=1, a694=1, a695=1, a696=1, a697=1, a698=1, a699=1, a700=1, a701=1, a702=1, a703=1, a704=1, a705=1, a706=1, a707=1, a708=1, a709=1, a710=1, a711=1, a712=1, a713=1, a714=1, a715=1, a716=1, a717=1, a718=1, a719=1, a720=1, a721=1, a722=1, a723=1, a724=1, a725=1, a726=1, a727=1, a728=1, a729=1, a730=1, a731=1, a732=1, a733=1, a734=1, a735=1, a736=1, a737=1, a738=1, a739=1, a740=1, a741=1, a742=1, a743=1, a744=1, a745=1, a746=1, a747=1, a748=1, a749=1, a750=1, a751=1, a752=1, a753=1, a754=1, a755=1, a756=1, a757=1, a758=1, a759=1, a760=1, a761=1, a762=1, a763=1, a764=1, a765=1, a766=1, a767=1, a768=1, a769=1, a770=1, a771=1, a772=1, a773=1, a774=1, a775=1, a776=1, a777=1, a778=1, a779=1, a780=1, a781=1, a782=1, a783=1, a784=1, a785=1, a786=1, a787=1, a788=1, a789=1, a790=1, a791=1, a792=1, a793=1, a794=1, a795=1, a796=1, a797=1, a798=1, a799=1, a800=1, a801=1, a802=1, a803=1, a804=1, a805=1, a806=1, a807=1, a808=1, a809=1, a810=1, a811=1, a812=1, a813=1, a814=1, a815=1, a816=1, a817=1, a818=1, a819=1, a820=1, a821=1, a822=1, a823=1, a824=1, a825=1, a826=1, a827=1, a828=1, a829=1, a830=1, a831=1, a832=1, a833=1, a834=1, a835=1, a836=1, a837=1, a838=1, a839=1, a840=1, a841=1, a842=1, a843=1, a844=1, a845=1, a846=1, a847=1, a848=1, a849=1, a850=1, a851=1, a852=1, a853=1, a854=1, a855=1, a856=1, a857=1, a858=1, a859=1, a860=1, a861=1, a862=1, a863=1, a864=1, a865=1, a866=1, a867=1, a868=1, a869=1, a870=1, a871=1, a872=1, a873=1, a874=1, a875=1, a876=1, a877=1, a878=1, a879=1, a880=1, a881=1, a882=1, a883=1, a884=1, a885=1, a886=1, a887=1, a888=1, a889=1, a890=1, a891=1, a892=1, a893=1, a894=1, a895=1, a896=1, a897=1, a898=1, a899=1, a900=1, a901=1, a902=1, a903=1, a904=1, a905=1, a906=1, a907=1, a908=1, a909=1, a910=1, a911=1, a912=1, a913=1, a914=1, a915=1, a916=1, a917=1, a918=1, a919=1, a920=1, a921=1, a922=1, a923=1, a924=1, a925=1, a926=1, a927=1, a928=1, a929=1, a930=1, a931=1, a932=1, a933=1, a934=1, a935=1, a936=1, a937=1, a938=1, a939=1, a940=1, a941=1, a942=1, a943=1, a944=1, a945=1, a946=1, a947=1, a948=1, a949=1, a950=1, a951=1, a952=1, a953=1, a954=1, a955=1, a956=1, a957=1, a958=1, a959=1, a960=1, a961=1, a962=1, a963=1, a964=1, a965=1, a966=1, a967=1, a968=1, a969=1, a970=1, a971=1, a972=1, a973=1, a974=1, a975=1, a976=1, a977=1, a978=1, a979=1, a980=1, a981=1, a982=1, a983=1, a984=1, a985=1, a986=1, a987=1, a988=1, a989=1, a990=1, a991=1, a992=1, a993=1, a994=1, a995=1, a996=1, a997=1, a998=1, a999=1, a1000=1, a1001=1, a1002=1, a1003=1, a1004=1, a1005=1, a1006=1, a1007=1, a1008=1, a1009=1, a1010=1, a1011=1, a1012=1, a1013=1, a1014=1, a1015=1, a1016=1, a1017=1, a1018=1, a1019=1, a1020=1, a1021=1, a1022=1, a1023=1
~~~

Result:
~~~
a0=1, a1=1, a2=1, a3=1, a4=1, a5=1, a6=1, a7=1, a8=1, a9=1, a10=1, a11=1, a12=1, a13=1, a14=1, a15=1, a16=1, a17=1, a18=1, a19=1, a20=1, a21=1, a22=1, a23=1, a24=1, a25=1, a26=1, a27=1, a28=1, a29=1, a30=1, a31=1, a32=1, a33=1, a34=1, a35=1, a36=1, a37=1, a38=1, a39=1, a40=1, a41=1, a42=1, a43=1, a44=1, a45=1, a46=1, a47=1, a48=1, a49=1, a50=1, a51=1, a52=1, a53=1, a54=1, a55=1, a56=1, a57=1, a58=1, a59=1, a60=1, a61=1, a62=1, a63=1, a64=1, a65=1, a66=1, a67=1, a68=1, a69=1, a70=1, a71=1, a72=1, a73=1, a74=1, a75=1, a76=1, a77=1, a78=1, a79=1, a80=1, a81=1, a82=1, a83=1, a84=1, a85=1, a86=1, a87=1, a88=1, a89=1, a90=1, a91=1, a92=1, a93=1, a94=1, a95=1, a96=1, a97=1, a98=1, a99=1, a100=1, a101=1, a102=1, a103=1, a104=1, a105=1, a106=1, a107=1, a108=1, a109=1, a110=1, a111=1, a112=1, a113=1, a114=1, a115=1, a116=1, a117=1, a118=1, a119=1, a120=1, a121=1, a122=1, a123=1, a124=1, a125=1, a126=1, a127=1, a128=1, a129=1, a130=1, a131=1, a132=1, a133=1, a134=1, a135=1, a136=1, a137=1, a138=1, a139=1, a140=1, a141=1, a142=1, a143=1, a144=1, a145=1, a146=1, a147=1, a148=1, a149=1, a150=1, a151=1, a152=1, a153=1, a154=1, a155=1, a156=1, a157=1, a158=1, a159=1, a160=1, a161=1, a162=1, a163=1, a164=1, a165=1, a166=1, a167=1, a168=1, a169=1, a170=1, a171=1, a172=1, a173=1, a174=1, a175=1, a176=1, a177=1, a178=1, a179=1, a180=1, a181=1, a182=1, a183=1, a184=1, a185=1, a186=1, a187=1, a188=1, a189=1, a190=1, a191=1, a192=1, a193=1, a194=1, a195=1, a196=1, a197=1, a198=1, a199=1, a200=1, a201=1, a202=1, a203=1, a204=1, a205=1, a206=1, a207=1, a208=1, a209=1, a210=1, a211=1, a212=1, a213=1, a214=1, a215=1, a216=1, a217=1, a218=1, a219=1, a220=1, a221=1, a222=1, a223=1, a224=1, a225=1, a226=1, a227=1, a228=1, a229=1, a230=1, a231=1, a232=1, a233=1, a234=1, a235=1, a236=1, a237=1, a238=1, a239=1, a240=1, a241=1, a242=1, a243=1, a244=1, a245=1, a246=1, a247=1, a248=1, a249=1, a250=1, a251=1, a252=1, a253=1, a254=1, a255=1, a256=1, a257=1, a258=1, a259=1, a260=1, a261=1, a262=1, a263=1, a264=1, a265=1, a266=1, a267=1, a268=1, a269=1, a270=1, a271=1, a272=1, a273=1, a274=1, a275=1, a276=1, a277=1, a278=1, a279=1, a280=1, a281=1, a282=1, a283=1, a284=1, a285=1, a286=1, a287=1, a288=1, a289=1, a290=1, a291=1, a292=1, a293=1, a294=1, a295=1, a296=1, a297=1, a298=1, a299=1, a300=1, a301=1, a302=1, a303=1, a304=1, a305=1, a306=1, a307=1, a308=1, a309=1, a310=1, a311=1, a312=1, a313=1, a314=1, a315=1, a316=1, a317=1, a318=1, a319=1, a320=1, a321=1, a322=1, a323=1, a324=1, a325=1, a326=1, a327=1, a328=1, a329=1, a330=1, a331=1, a332=1, a333=1, a334=1, a335=1, a336=1, a337=1, a338=1, a339=1, a340=1, a341=1, a342=1, a343=1, a344=1, a345=1, a346=1, a347=1, a348=1, a349=1, a350=1, a351=1, a352=1, a353=1, a354=1, a355=1, a356=1, a357=1, a358=1, a359=1, a360=1, a361=1, a362=1, a363=1, a364=1, a365=1, a366=1, a367=1, a368=1, a369=1, a370=1, a371=1, a372=1, a373=1, a374=1, a375=1, a376=1, a377=1, a378=1, a379=1, a380=1, a381=1, a382=1, a383=1, a384=1, a385=1, a386=1, a387=1, a388=1, a389=1, a390=1, a391=1, a392=1, a393=1, a394=1, a395=1, a396=1, a397=1, a398=1, a399=1, a400=1, a401=1, a402=1, a403=1, a404=1, a405=1, a406=1, a407=1, a408=1, a409=1, a410=1, a411=1, a412=1, a413=1, a414=1, a415=1, a416=1, a417=1, a418=1, a419=1, a420=1, a421=1, a422=1, a423=1, a424=1, a425=1, a426=1, a427=1, a428=1, a429=1, a430=1, a431=1, a432=1, a433=1, a434=1, a435=1, a436=1, a437=1, a438=1, a439=1, a440=1, a441=1, a442=1, a443=1, a444=1, a445=1, a446=1, a447=1, a448=1, a449=1, a450=1, a451=1, a452=1, a453=1, a454=1, a455=1, a456=1, a457=1, a458=1, a459=1, a460=1, a461=1, a462=1, a463=1, a464=1, a465=1, a466=1, a467=1, a468=1, a469=1, a470=1, a471=1, a472=1, a473=1, a474=1, a475=1, a476=1, a477=1, a478=1, a479=1, a480=1, a481=1, a482=1, a483=1, a484=1, a485=1, a486=1, a487=1, a488=1, a489=1, a490=1, a491=1, a492=1, a493=1, a494=1, a495=1, a496=1, a497=1, a498=1, a499=1, a500=1, a501=1, a502=1, a503=1, a504=1, a505=1, a506=1, a507=1, a508=1, a509=1, a510=1, a511=1, a512=1, a513=1, a514=1, a515=1, a516=1, a517=1, a518=1, a519=1, a520=1, a521=1, a522=1, a523=1, a524=1, a525=1, a526=1, a527=1, a528=1, a529=1, a530=1, a531=1, a532=1, a533=1, a534=1, a535=1, a536=1, a537=1, a538=1, a539=1, a540=1, a541=1, a542=1, a543=1, a544=1, a545=1, a546=1, a547=1, a548=1, a549=1, a550=1, a551=1, a552=1, a553=1, a554=1, a555=1, a556=1, a557=1, a558=1, a559=1, a560=1, a561=1, a562=1, a563=1, a564=1, a565=1, a566=1, a567=1, a568=1, a569=1, a570=1, a571=1, a572=1, a573=1, a574=1, a575=1, a576=1, a577=1, a578=1, a579=1, a580=1, a581=1, a582=1, a583=1, a584=1, a585=1, a586=1, a587=1, a588=1, a589=1, a590=1, a591=1, a592=1, a593=1, a594=1, a595=1, a596=1, a597=1, a598=1, a599=1, a600=1, a601=1, a602=1, a603=1, a604=1, a605=1, a606=1, a607=1, a608=1, a609=1, a610=1, a611=1, a612=1, a613=1, a614=1, a615=1, a616=1, a617=1, a618=1, a619=1, a620=1, a621=1, a622=1, a623=1, a624=1, a625=1, a626=1, a627=1, a628=1, a629=1, a630=1, a631=1, a632=1, a633=1, a634=1, a635=1, a636=1, a637=1, a638=1, a639=1, a640=1, a641=1, a642=1, a643=1, a644=1, a645=1, a646=1, a647=1, a648=1, a649=1, a650=1, a651=1, a652=1, a653=1, a654=1, a655=1, a656=1, a657=1, a658=1, a659=1, a660=1, a661=1, a662=1, a663=1, a664=1, a665=1, a666=1, a667=1, a668=1, a669=1, a670=1, a671=1, a672=1, a673=1, a674=1, a675=1, a676=1, a677=1, a678=1, a679=1, a680=1, a681=1, a682=1, a683=1, a684=1, a685=1, a686=1, a687=1, a688=1, a689=1, a690=1, a691=1, a692=1, a693=1, a694=1, a695=1, a696=1, a697=1, a698=1, a699=1, a700=1, a701=1, a702=1, a703=1, a704=1, a705=1, a706=1, a707=1, a708=1, a709=1, a710=1, a711=1, a712=1, a713=1, a714=1, a715=1, a716=1, a717=1, a718=1, a719=1, a720=1, a721=1, a722=1, a723=1, a724=1, a725=1, a726=1, a727=1, a728=1, a729=1, a730=1, a731=1, a732=1, a733=1, a734=1, a735=1, a736=1, a737=1, a738=1, a739=1, a740=1, a741=1, a742=1, a743=1, a744=1, a745=1, a746=1, a747=1, a748=1, a749=1, a750=1, a751=1, a752=1, a753=1, a754=1, a755=1, a756=1, a757=1, a758=1, a759=1, a760=1, a761=1, a762=1, a763=1, a764=1, a765=1, a766=1, a767=1, a768=1, a769=1, a770=1, a771=1, a772=1, a773=1, a774=1, a775=1, a776=1, a777=1, a778=1, a779=1, a780=1, a781=1, a782=1, a783=1, a784=1, a785=1, a786=1, a787=1, a788=1, a789=1, a790=1, a791=1, a792=1, a793=1, a794=1, a795=1, a796=1, a797=1, a798=1, a799=1, a800=1, a801=1, a802=1, a803=1, a804=1, a805=1, a806=1, a807=1, a808=1, a809=1, a810=1, a811=1, a812=1, a813=1, a814=1, a815=1, a816=1, a817=1, a818=1, a819=1, a820=1, a821=1, a822=1, a823=1, a824=1, a825=1, a826=1, a827=1, a828=1, a829=1, a830=1, a831=1, a832=1, a833=1, a834=1, a835=1, a836=1, a837=1, a838=1, a839=1, a840=1, a841=1, a842=1, a843=1, a844=1, a845=1, a846=1, a847=1, a848=1, a849=1, a850=1, a851=1, a852=1, a853=1, a854=1, a855=1, a856=1, a857=1, a858=1, a859=1, a860=1, a861=1, a862=1, a863=1, a864=1, a865=1, a866=1, a867=1, a868=1, a869=1, a870=1, a871=1, a872=1, a873=1, a874=1, a875=1, a876=1, a877=1, a878=1, a879=1, a880=1, a881=1, a882=1, a883=1, a884=1, a885=1, a886=1, a887=1, a888=1, a889=1, a890=1, a891=1, a892=1, a893=1, a894=1, a895=1, a896=1, a897=1, a898=1, a899=1, a900=1, a901=1, a902=1, a903=1, a904=1, a905=1, a906=1, a907=1, a908=1, a909=1, a910=1, a911=1, a912=1, a913=1, a914=1, a915=1, a916=1, a917=1, a918=1, a919=1, a920=1, a921=1, a922=1, a923=1, a924=1, a925=1, a926=1, a927=1, a928=1, a929=1, a930=1, a931=1, a932=1, a933=1, a934=1, a935=1, a936=1, a937=1, a938=1, a939=1, a940=1, a941=1, a942=1, a943=1, a944=1, a945=1, a946=1, a947=1, a948=1, a949=1, a950=1, a951=1, a952=1, a953=1, a954=1, a955=1, a956=1, a957=1, a958=1, a959=1, a960=1, a961=1, a962=1, a963=1, a964=1, a965=1, a966=1, a967=1, a968=1, a969=1, a970=1, a971=1, a972=1, a973=1, a974=1, a975=1, a976=1, a977=1, a978=1, a979=1, a980=1, a981=1, a982=1, a983=1, a984=1, a985=1, a986=1, a987=1, a988=1, a989=1, a990=1, a991=1, a992=1, a993=1, a994=1, a995=1, a996=1, a997=1, a998=1, a999=1, a1000=1, a1001=1, a1002=1, a1003=1, a1004=1, a1005=1, a1006=1, a1007=1, a1008=1, a1009=1, a1010=1, a1011=1, a1012=1, a1013=1, a1014=1, a1015=1, a1016=1, a1017=1, a1018=1, a1019=1, a1020=1, a1021=1, a1022=1, a1023=1
~~~

### large dictionary key

Input:
~~~
aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=1
~~~

Result:
~~~
aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=1
~~~

### large list

Input:
~~~
a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, a29, a30, a31, a32, a33, a34, a35, a36, a37, a38, a39, a40, a41, a42, a43, a44, a45, a46, a47, a48, a49, a50, a51, a52, a53, a54, a55, a56, a57, a58, a59, a60, a61, a62, a63, a64, a65, a66, a67, a68, a69, a70, a71, a72, a73, a74, a75, a76, a77, a78, a79, a80, a81, a82, a83, a84, a85, a86, a87, a88, a89, a90, a91, a92, a93, a94, a95, a96, a97, a98, a99, a100, a101, a102, a103, a104, a105, a106, a107, a108, a109, a110, a111, a112, a113, a114, a115, a116, a117, a118, a119, a120, a121, a122, a123, a124, a125, a126, a127, a128, a129, a130, a131, a132, a133, a134, a135, a136, a137, a138, a139, a140, a141, a142, a143, a144, a145, a146, a147, a148, a149, a150, a151, a152, a153, a154, a155, a156, a157, a158, a159, a160, a161, a162, a163, a164, a165, a166, a167, a168, a169, a170, a171, a172, a173, a174, a175, a176, a177, a178, a179, a180, a181, a182, a183, a184, a185, a186, a187, a188, a189, a190, a191, a192, a193, a194, a195, a196, a197, a198, a199, a200, a201, a202, a203, a204, a205, a206, a207, a208, a209, a210, a211, a212, a213, a214, a215, a216, a217, a218, a219, a220, a221, a222, a223, a224, a225, a226, a227, a228, a229, a230, a231, a232, a233, a234, a235, a236, a237, a238, a239, a240, a241, a242, a243, a244, a245, a246, a247, a248, a249, a250, a251, a252, a253, a254, a255, a256, a257, a258, a259, a260, a261, a262, a263, a264, a265, a266, a267, a268, a269, a270, a271, a272, a273, a274, a275, a276, a277, a278, a279, a280, a281, a282, a283, a284, a285, a286, a287, a288, a289, a290, a291, a292, a293, a294, a295, a296, a297, a298, a299, a300, a301, a302, a303, a304, a305, a306, a307, a308, a309, a310, a311, a312, a313, a314, a315, a316, a317, a318, a319, a320, a321, a322, a323, a324, a325, a326, a327, a328, a329, a330, a331, a332, a333, a334, a335, a336, a337, a338, a339, a340, a341, a342, a343, a344, a345, a346, a347, a348, a349, a350, a351, a352, a353, a354, a355, a356, a357, a358, a359, a360, a361, a362, a363, a364, a365, a366, a367, a368, a369, a370, a371, a372, a373, a374, a375, a376, a377, a378, a379, a380, a381, a382, a383, a384, a385, a386, a387, a388, a389, a390, a391, a392, a393, a394, a395, a396, a397, a398, a399, a400, a401, a402, a403, a404, a405, a406, a407, a408, a409, a410, a411, a412, a413, a414, a415, a416, a417, a418, a419, a420, a421, a422, a423, a424, a425, a426, a427, a428, a429, a430, a431, a432, a433, a434, a435, a436, a437, a438, a439, a440, a441, a442, a443, a444, a445, a446, a447, a448, a449, a450, a451, a452, a453, a454, a455, a456, a457, a458, a459, a460, a461, a462, a463, a464, a465, a466, a467, a468, a469, a470, a471, a472, a473, a474, a475, a476, a477, a478, a479, a480, a481, a482, a483, a484, a485, a486, a487, a488, a489, a490, a491, a492, a493, a494, a495, a496, a497, a498, a499, a500, a501, a502, a503, a504, a505, a506, a507, a508, a509, a510, a511, a512, a513, a514, a515, a516, a517, a518, a519, a520, a521, a522, a523, a524, a525, a526, a527, a528, a529, a530, a531, a532, a533, a534, a535, a536, a537, a538, a539, a540, a541, a542, a543, a544, a545, a546, a547, a548, a549, a550, a551, a552, a553, a554, a555, a556, a557, a558, a559, a560, a561, a562, a563, a564, a565, a566, a567, a568, a569, a570, a571, a572, a573, a574, a575, a576, a577, a578, a579, a580, a581, a582, a583, a584, a585, a586, a587, a588, a589, a590, a591, a592, a593, a594, a595, a596, a597, a598, a599, a600, a601, a602, a603, a604, a605, a606, a607, a608, a609, a610, a611, a612, a613, a614, a615, a616, a617, a618, a619, a620, a621, a622, a623, a624, a625, a626, a627, a628, a629, a630, a631, a632, a633, a634, a635, a636, a637, a638, a639, a640, a641, a642, a643, a644, a645, a646, a647, a648, a649, a650, a651, a652, a653, a654, a655, a656, a657, a658, a659, a660, a661, a662, a663, a664, a665, a666, a667, a668, a669, a670, a671, a672, a673, a674, a675, a676, a677, a678, a679, a680, a681, a682, a683, a684, a685, a686, a687, a688, a689, a690, a691, a692, a693, a694, a695, a696, a697, a698, a699, a700, a701, a702, a703, a704, a705, a706, a707, a708, a709, a710, a711, a712, a713, a714, a715, a716, a717, a718, a719, a720, a721, a722, a723, a724, a725, a726, a727, a728, a729, a730, a731, a732, a733, a734, a735, a736, a737, a738, a739, a740, a741, a742, a743, a744, a745, a746, a747, a748, a749, a750, a751, a752, a753, a754, a755, a756, a757, a758, a759, a760, a761, a762, a763, a764, a765, a766, a767, a768, a769, a770, a771, a772, a773, a774, a775, a776, a777, a778, a779, a780, a781, a782, a783, a784, a785, a786, a787, a788, a789, a790, a791, a792, a793, a794, a795, a796, a797, a798, a799, a800, a801, a802, a803, a804, a805, a806, a807, a808, a809, a810, a811, a812, a813, a814, a815, a816, a817, a818, a819, a820, a821, a822, a823, a824, a825, a826, a827, a828, a829, a830, a831, a832, a833, a834, a835, a836, a837, a838, a839, a840, a841, a842, a843, a844, a845, a846, a847, a848, a849, a850, a851, a852, a853, a854, a855, a856, a857, a858, a859, a860, a861, a862, a863, a864, a865, a866, a867, a868, a869, a870, a871, a872, a873, a874, a875, a876, a877, a878, a879, a880, a881, a882, a883, a884, a885, a886, a887, a888, a889, a890, a891, a892, a893, a894, a895, a896, a897, a898, a899, a900, a901, a902, a903, a904, a905, a906, a907, a908, a909, a910, a911, a912, a913, a914, a915, a916, a917, a918, a919, a920, a921, a922, a923, a924, a925, a926, a927, a928, a929, a930, a931, a932, a933, a934, a935, a936, a937, a938, a939, a940, a941, a942, a943, a944, a945, a946, a947, a948, a949, a950, a951, a952, a953, a954, a955, a956, a957, a958, a959, a960, a961, a962, a963, a964, a965, a966, a967, a968, a969, a970, a971, a972, a973, a974, a975, a976, a977, a978, a979, a980, a981, a982, a983, a984, a985, a986, a987, a988, a989, a990, a991, a992, a993, a994, a995, a996, a997, a998, a999, a1000, a1001, a1002, a1003, a1004, a1005, a1006, a1007, a1008, a1009, a1010, a1011, a1012, a1013, a1014, a1015, a1016, a1017, a1018, a1019, a1020, a1021, a1022, a1023
~~~

Result:
~~~
a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, a29, a30, a31, a32, a33, a34, a35, a36, a37, a38, a39, a40, a41, a42, a43, a44, a45, a46, a47, a48, a49, a50, a51, a52, a53, a54, a55, a56, a57, a58, a59, a60, a61, a62, a63, a64, a65, a66, a67, a68, a69, a70, a71, a72, a73, a74, a75, a76, a77, a78, a79, a80, a81, a82, a83, a84, a85, a86, a87, a88, a89, a90, a91, a92, a93, a94, a95, a96, a97, a98, a99, a100, a101, a102, a103, a104, a105, a106, a107, a108, a109, a110, a111, a112, a113, a114, a115, a116, a117, a118, a119, a120, a121, a122, a123, a124, a125, a126, a127, a128, a129, a130, a131, a132, a133, a134, a135, a136, a137, a138, a139, a140, a141, a142, a143, a144, a145, a146, a147, a148, a149, a150, a151, a152, a153, a154, a155, a156, a157, a158, a159, a160, a161, a162, a163, a164, a165, a166, a167, a168, a169, a170, a171, a172, a173, a174, a175, a176, a177, a178, a179, a180, a181, a182, a183, a184, a185, a186, a187, a188, a189, a190, a191, a192, a193, a194, a195, a196, a197, a198, a199, a200, a201, a202, a203, a204, a205, a206, a207, a208, a209, a210, a211, a212, a213, a214, a215, a216, a217, a218, a219, a220, a221, a222, a223, a224, a225, a226, a227, a228, a229, a230, a231, a232, a233, a234, a235, a236, a237, a238, a239, a240, a241, a242, a243, a244, a245, a246, a247, a248, a249, a250, a251, a252, a253, a254, a255, a256, a257, a258, a259, a260, a261, a262, a263, a264, a265, a266, a267, a268, a269, a270, a271, a272, a273, a274, a275, a276, a277, a278, a279, a280, a281, a282, a283, a284, a285, a286, a287, a288, a289, a290, a291, a292, a293, a294, a295, a296, a297, a298, a299, a300, a301, a302, a303, a304, a305, a306, a307, a308, a309, a310, a311, a312, a313, a314, a315, a316, a317, a318, a319, a320, a321, a322, a323, a324, a325, a326, a327, a328, a329, a330, a331, a332, a333, a334, a335, a336, a337, a338, a339, a340, a341, a342, a343, a344, a345, a346, a347, a348, a349, a350, a351, a352, a353, a354, a355, a356, a357, a358, a359, a360, a361, a362, a363, a364, a365, a366, a367, a368, a369, a370, a371, a372, a373, a374, a375, a376, a377, a378, a379, a380, a381, a382, a383, a384, a385, a386, a387, a388, a389, a390, a391, a392, a393, a394, a395, a396, a397, a398, a399, a400, a401, a402, a403, a404, a405, a406, a407, a408, a409, a410, a411, a412, a413, a414, a415, a416, a417, a418, a419, a420, a421, a422, a423, a424, a425, a426, a427, a428, a429, a430, a431, a432, a433, a434, a435, a436, a437, a438, a439, a440, a441, a442, a443, a444, a445, a446, a447, a448, a449, a450, a451, a452, a453, a454, a455, a456, a457, a458, a459, a460, a461, a462, a463, a464, a465, a466, a467, a468, a469, a470, a471, a472, a473, a474, a475, a476, a477, a478, a479, a480, a481, a482, a483, a484, a485, a486, a487, a488, a489, a490, a491, a492, a493, a494, a495, a496, a497, a498, a499, a500, a501, a502, a503, a504, a505, a506, a507, a508, a509, a510, a511, a512, a513, a514, a515, a516, a517, a518, a519, a520, a521, a522, a523, a524, a525, a526, a527, a528, a529, a530, a531, a532, a533, a534, a535, a536, a537, a538, a539, a540, a541, a542, a543, a544, a545, a546, a547, a548, a549, a550, a551, a552, a553, a554, a555, a556, a557, a558, a559, a560, a561, a562, a563, a564, a565, a566, a567, a568, a569, a570, a571, a572, a573, a574, a575, a576, a577, a578, a579, a580, a581, a582, a583, a584, a585, a586, a587, a588, a589, a590, a591, a592, a593, a594, a595, a596, a597, a598, a599, a600, a601, a602, a603, a604, a605, a606, a607, a608, a609, a610, a611, a612, a613, a614, a615, a616, a617, a618, a619, a620, a621, a622, a623, a624, a625, a626, a627, a628, a629, a630, a631, a632, a633, a634, a635, a636, a637, a638, a639, a640, a641, a642, a643, a644, a645, a646, a647, a648, a649, a650, a651, a652, a653, a654, a655, a656, a657, a658, a659, a660, a661, a662, a663, a664, a665, a666, a667, a668, a669, a670, a671, a672, a673, a674, a675, a676, a677, a678, a679, a680, a681, a682, a683, a684, a685, a686, a687, a688, a689, a690, a691, a692, a693, a694, a695, a696, a697, a698, a699, a700, a701, a702, a703, a704, a705, a706, a707, a708, a709, a710, a711, a712, a713, a714, a715, a716, a717, a718, a719, a720, a721, a722, a723, a724, a725, a726, a727, a728, a729, a730, a731, a732, a733, a734, a735, a736, a737, a738, a739, a740, a741, a742, a743, a744, a745, a746, a747, a748, a749, a750, a751, a752, a753, a754, a755, a756, a757, a758, a759, a760, a761, a762, a763, a764, a765, a766, a767, a768, a769, a770, a771, a772, a773, a774, a775, a776, a777, a778, a779, a780, a781, a782, a783, a784, a785, a786, a787, a788, a789, a790, a791, a792, a793, a794, a795, a796, a797, a798, a799, a800, a801, a802, a803, a804, a805, a806, a807, a808, a809, a810, a811, a812, a813, a814, a815, a816, a817, a818, a819, a820, a821, a822, a823, a824, a825, a826, a827, a828, a829, a830, a831, a832, a833, a834, a835, a836, a837, a838, a839, a840, a841, a842, a843, a844, a845, a846, a847, a848, a849, a850, a851, a852, a853, a854, a855, a856, a857, a858, a859, a860, a861, a862, a863, a864, a865, a866, a867, a868, a869, a870, a871, a872, a873, a874, a875, a876, a877, a878, a879, a880, a881, a882, a883, a884, a885, a886, a887, a888, a889, a890, a891, a892, a893, a894, a895, a896, a897, a898, a899, a900, a901, a902, a903, a904, a905, a906, a907, a908, a909, a910, a911, a912, a913, a914, a915, a916, a917, a918, a919, a920, a921, a922, a923, a924, a925, a926, a927, a928, a929, a930, a931, a932, a933, a934, a935, a936, a937, a938, a939, a940, a941, a942, a943, a944, a945, a946, a947, a948, a949, a950, a951, a952, a953, a954, a955, a956, a957, a958, a959, a960, a961, a962, a963, a964, a965, a966, a967, a968, a969, a970, a971, a972, a973, a974, a975, a976, a977, a978, a979, a980, a981, a982, a983, a984, a985, a986, a987, a988, a989, a990, a991, a992, a993, a994, a995, a996, a997, a998, a999, a1000, a1001, a1002, a1003, a1004, a1005, a1006, a1007, a1008, a1009, a1010, a1011, a1012, a1013, a1014, a1015, a1016, a1017, a1018, a1019, a1020, a1021, a1022, a1023
~~~

### large parameterised list

Input:
~~~
foo;a0=1, foo;a1=1, foo;a2=1, foo;a3=1, foo;a4=1, foo;a5=1, foo;a6=1, foo;a7=1, foo;a8=1, foo;a9=1, foo;a10=1, foo;a11=1, foo;a12=1, foo;a13=1, foo;a14=1, foo;a15=1, foo;a16=1, foo;a17=1, foo;a18=1, foo;a19=1, foo;a20=1, foo;a21=1, foo;a22=1, foo;a23=1, foo;a24=1, foo;a25=1, foo;a26=1, foo;a27=1, foo;a28=1, foo;a29=1, foo;a30=1, foo;a31=1, foo;a32=1, foo;a33=1, foo;a34=1, foo;a35=1, foo;a36=1, foo;a37=1, foo;a38=1, foo;a39=1, foo;a40=1, foo;a41=1, foo;a42=1, foo;a43=1, foo;a44=1, foo;a45=1, foo;a46=1, foo;a47=1, foo;a48=1, foo;a49=1, foo;a50=1, foo;a51=1, foo;a52=1, foo;a53=1, foo;a54=1, foo;a55=1, foo;a56=1, foo;a57=1, foo;a58=1, foo;a59=1, foo;a60=1, foo;a61=1, foo;a62=1, foo;a63=1, foo;a64=1, foo;a65=1, foo;a66=1, foo;a67=1, foo;a68=1, foo;a69=1, foo;a70=1, foo;a71=1, foo;a72=1, foo;a73=1, foo;a74=1, foo;a75=1, foo;a76=1, foo;a77=1, foo;a78=1, foo;a79=1, foo;a80=1, foo;a81=1, foo;a82=1, foo;a83=1, foo;a84=1, foo;a85=1, foo;a86=1, foo;a87=1, foo;a88=1, foo;a89=1, foo;a90=1, foo;a91=1, foo;a92=1, foo;a93=1, foo;a94=1, foo;a95=1, foo;a96=1, foo;a97=1, foo;a98=1, foo;a99=1, foo;a100=1, foo;a101=1, foo;a102=1, foo;a103=1, foo;a104=1, foo;a105=1, foo;a106=1, foo;a107=1, foo;a108=1, foo;a109=1, foo;a110=1, foo;a111=1, foo;a112=1, foo;a113=1, foo;a114=1, foo;a115=1, foo;a116=1, foo;a117=1, foo;a118=1, foo;a119=1, foo;a120=1, foo;a121=1, foo;a122=1, foo;a123=1, foo;a124=1, foo;a125=1, foo;a126=1, foo;a127=1, foo;a128=1, foo;a129=1, foo;a130=1, foo;a131=1, foo;a132=1, foo;a133=1, foo;a134=1, foo;a135=1, foo;a136=1, foo;a137=1, foo;a138=1, foo;a139=1, foo;a140=1, foo;a141=1, foo;a142=1, foo;a143=1, foo;a144=1, foo;a145=1, foo;a146=1, foo;a147=1, foo;a148=1, foo;a149=1, foo;a150=1, foo;a151=1, foo;a152=1, foo;a153=1, foo;a154=1, foo;a155=1, foo;a156=1, foo;a157=1, foo;a158=1, foo;a159=1, foo;a160=1, foo;a161=1, foo;a162=1, foo;a163=1, foo;a164=1, foo;a165=1, foo;a166=1, foo;a167=1, foo;a168=1, foo;a169=1, foo;a170=1, foo;a171=1, foo;a172=1, foo;a173=1, foo;a174=1, foo;a175=1, foo;a176=1, foo;a177=1, foo;a178=1, foo;a179=1, foo;a180=1, foo;a181=1, foo;a182=1, foo;a183=1, foo;a184=1, foo;a185=1, foo;a186=1, foo;a187=1, foo;a188=1, foo;a189=1, foo;a190=1, foo;a191=1, foo;a192=1, foo;a193=1, foo;a194=1, foo;a195=1, foo;a196=1, foo;a197=1, foo;a198=1, foo;a199=1, foo;a200=1, foo;a201=1, foo;a202=1, foo;a203=1, foo;a204=1, foo;a205=1, foo;a206=1, foo;a207=1, foo;a208=1, foo;a209=1, foo;a210=1, foo;a211=1, foo;a212=1, foo;a213=1, foo;a214=1, foo;a215=1, foo;a216=1, foo;a217=1, foo;a218=1, foo;a219=1, foo;a220=1, foo;a221=1, foo;a222=1, foo;a223=1, foo;a224=1, foo;a225=1, foo;a226=1, foo;a227=1, foo;a228=1, foo;a229=1, foo;a230=1, foo;a231=1, foo;a232=1, foo;a233=1, foo;a234=1, foo;a235=1, foo;a236=1, foo;a237=1, foo;a238=1, foo;a239=1, foo;a240=1, foo;a241=1, foo;a242=1, foo;a243=1, foo;a244=1, foo;a245=1, foo;a246=1, foo;a247=1, foo;a248=1, foo;a249=1, foo;a250=1, foo;a251=1, foo;a252=1, foo;a253=1, foo;a254=1, foo;a255=1, foo;a256=1, foo;a257=1, foo;a258=1, foo;a259=1, foo;a260=1, foo;a261=1, foo;a262=1, foo;a263=1, foo;a264=1, foo;a265=1, foo;a266=1, foo;a267=1, foo;a268=1, foo;a269=1, foo;a270=1, foo;a271=1, foo;a272=1, foo;a273=1, foo;a274=1, foo;a275=1, foo;a276=1, foo;a277=1, foo;a278=1, foo;a279=1, foo;a280=1, foo;a281=1, foo;a282=1, foo;a283=1, foo;a284=1, foo;a285=1, foo;a286=1, foo;a287=1, foo;a288=1, foo;a289=1, foo;a290=1, foo;a291=1, foo;a292=1, foo;a293=1, foo;a294=1, foo;a295=1, foo;a296=1, foo;a297=1, foo;a298=1, foo;a299=1, foo;a300=1, foo;a301=1, foo;a302=1, foo;a303=1, foo;a304=1, foo;a305=1, foo;a306=1, foo;a307=1, foo;a308=1, foo;a309=1, foo;a310=1, foo;a311=1, foo;a312=1, foo;a313=1, foo;a314=1, foo;a315=1, foo;a316=1, foo;a317=1, foo;a318=1, foo;a319=1, foo;a320=1, foo;a321=1, foo;a322=1, foo;a323=1, foo;a324=1, foo;a325=1, foo;a326=1, foo;a327=1, foo;a328=1, foo;a329=1, foo;a330=1, foo;a331=1, foo;a332=1, foo;a333=1, foo;a334=1, foo;a335=1, foo;a336=1, foo;a337=1, foo;a338=1, foo;a339=1, foo;a340=1, foo;a341=1, foo;a342=1, foo;a343=1, foo;a344=1, foo;a345=1, foo;a346=1, foo;a347=1, foo;a348=1, foo;a349=1, foo;a350=1, foo;a351=1, foo;a352=1, foo;a353=1, foo;a354=1, foo;a355=1, foo;a356=1, foo;a357=1, foo;a358=1, foo;a359=1, foo;a360=1, foo;a361=1, foo;a362=1, foo;a363=1, foo;a364=1, foo;a365=1, foo;a366=1, foo;a367=1, foo;a368=1, foo;a369=1, foo;a370=1, foo;a371=1, foo;a372=1, foo;a373=1, foo;a374=1, foo;a375=1, foo;a376=1, foo;a377=1, foo;a378=1, foo;a379=1, foo;a380=1, foo;a381=1, foo;a382=1, foo;a383=1, foo;a384=1, foo;a385=1, foo;a386=1, foo;a387=1, foo;a388=1, foo;a389=1, foo;a390=1, foo;a391=1, foo;a392=1, foo;a393=1, foo;a394=1, foo;a395=1, foo;a396=1, foo;a397=1, foo;a398=1, foo;a399=1, foo;a400=1, foo;a401=1, foo;a402=1, foo;a403=1, foo;a404=1, foo;a405=1, foo;a406=1, foo;a407=1, foo;a408=1, foo;a409=1, foo;a410=1, foo;a411=1, foo;a412=1, foo;a413=1, foo;a414=1, foo;a415=1, foo;a416=1, foo;a417=1, foo;a418=1, foo;a419=1, foo;a420=1, foo;a421=1, foo;a422=1, foo;a423=1, foo;a424=1, foo;a425=1, foo;a426=1, foo;a427=1, foo;a428=1, foo;a429=1, foo;a430=1, foo;a431=1, foo;a432=1, foo;a433=1, foo;a434=1, foo;a435=1, foo;a436=1, foo;a437=1, foo;a438=1, foo;a439=1, foo;a440=1, foo;a441=1, foo;a442=1, foo;a443=1, foo;a444=1, foo;a445=1, foo;a446=1, foo;a447=1, foo;a448=1, foo;a449=1, foo;a450=1, foo;a451=1, foo;a452=1, foo;a453=1, foo;a454=1, foo;a455=1, foo;a456=1, foo;a457=1, foo;a458=1, foo;a459=1, foo;a460=1, foo;a461=1, foo;a462=1, foo;a463=1, foo;a464=1, foo;a465=1, foo;a466=1, foo;a467=1, foo;a468=1, foo;a469=1, foo;a470=1, foo;a471=1, foo;a472=1, foo;a473=1, foo;a474=1, foo;a475=1, foo;a476=1, foo;a477=1, foo;a478=1, foo;a479=1, foo;a480=1, foo;a481=1, foo;a482=1, foo;a483=1, foo;a484=1, foo;a485=1, foo;a486=1, foo;a487=1, foo;a488=1, foo;a489=1, foo;a490=1, foo;a491=1, foo;a492=1, foo;a493=1, foo;a494=1, foo;a495=1, foo;a496=1, foo;a497=1, foo;a498=1, foo;a499=1, foo;a500=1, foo;a501=1, foo;a502=1, foo;a503=1, foo;a504=1, foo;a505=1, foo;a506=1, foo;a507=1, foo;a508=1, foo;a509=1, foo;a510=1, foo;a511=1, foo;a512=1, foo;a513=1, foo;a514=1, foo;a515=1, foo;a516=1, foo;a517=1, foo;a518=1, foo;a519=1, foo;a520=1, foo;a521=1, foo;a522=1, foo;a523=1, foo;a524=1, foo;a525=1, foo;a526=1, foo;a527=1, foo;a528=1, foo;a529=1, foo;a530=1, foo;a531=1, foo;a532=1, foo;a533=1, foo;a534=1, foo;a535=1, foo;a536=1, foo;a537=1, foo;a538=1, foo;a539=1, foo;a540=1, foo;a541=1, foo;a542=1, foo;a543=1, foo;a544=1, foo;a545=1, foo;a546=1, foo;a547=1, foo;a548=1, foo;a549=1, foo;a550=1, foo;a551=1, foo;a552=1, foo;a553=1, foo;a554=1, foo;a555=1, foo;a556=1, foo;a557=1, foo;a558=1, foo;a559=1, foo;a560=1, foo;a561=1, foo;a562=1, foo;a563=1, foo;a564=1, foo;a565=1, foo;a566=1, foo;a567=1, foo;a568=1, foo;a569=1, foo;a570=1, foo;a571=1, foo;a572=1, foo;a573=1, foo;a574=1, foo;a575=1, foo;a576=1, foo;a577=1, foo;a578=1, foo;a579=1, foo;a580=1, foo;a581=1, foo;a582=1, foo;a583=1, foo;a584=1, foo;a585=1, foo;a586=1, foo;a587=1, foo;a588=1, foo;a589=1, foo;a590=1, foo;a591=1, foo;a592=1, foo;a593=1, foo;a594=1, foo;a595=1, foo;a596=1, foo;a597=1, foo;a598=1, foo;a599=1, foo;a600=1, foo;a601=1, foo;a602=1, foo;a603=1, foo;a604=1, foo;a605=1, foo;a606=1, foo;a607=1, foo;a608=1, foo;a609=1, foo;a610=1, foo;a611=1, foo;a612=1, foo;a613=1, foo;a614=1, foo;a615=1, foo;a616=1, foo;a617=1, foo;a618=1, foo;a619=1, foo;a620=1, foo;a621=1, foo;a622=1, foo;a623=1, foo;a624=1, foo;a625=1, foo;a626=1, foo;a627=1, foo;a628=1, foo;a629=1, foo;a630=1, foo;a631=1, foo;a632=1, foo;a633=1, foo;a634=1, foo;a635=1, foo;a636=1, foo;a637=1, foo;a638=1, foo;a639=1, foo;a640=1, foo;a641=1, foo;a642=1, foo;a643=1, foo;a644=1, foo;a645=1, foo;a646=1, foo;a647=1, foo;a648=1, foo;a649=1, foo;a650=1, foo;a651=1, foo;a652=1, foo;a653=1, foo;a654=1, foo;a655=1, foo;a656=1, foo;a657=1, foo;a658=1, foo;a659=1, foo;a660=1, foo;a661=1, foo;a662=1, foo;a663=1, foo;a664=1, foo;a665=1, foo;a666=1, foo;a667=1, foo;a668=1, foo;a669=1, foo;a670=1, foo;a671=1, foo;a672=1, foo;a673=1, foo;a674=1, foo;a675=1, foo;a676=1, foo;a677=1, foo;a678=1, foo;a679=1, foo;a680=1, foo;a681=1, foo;a682=1, foo;a683=1, foo;a684=1, foo;a685=1, foo;a686=1, foo;a687=1, foo;a688=1, foo;a689=1, foo;a690=1, foo;a691=1, foo;a692=1, foo;a693=1, foo;a694=1, foo;a695=1, foo;a696=1, foo;a697=1, foo;a698=1, foo;a699=1, foo;a700=1, foo;a701=1, foo;a702=1, foo;a703=1, foo;a704=1, foo;a705=1, foo;a706=1, foo;a707=1, foo;a708=1, foo;a709=1, foo;a710=1, foo;a711=1, foo;a712=1, foo;a713=1, foo;a714=1, foo;a715=1, foo;a716=1, foo;a717=1, foo;a718=1, foo;a719=1, foo;a720=1, foo;a721=1, foo;a722=1, foo;a723=1, foo;a724=1, foo;a725=1, foo;a726=1, foo;a727=1, foo;a728=1, foo;a729=1, foo;a730=1, foo;a731=1, foo;a732=1, foo;a733=1, foo;a734=1, foo;a735=1, foo;a736=1, foo;a737=1, foo;a738=1, foo;a739=1, foo;a740=1, foo;a741=1, foo;a742=1, foo;a743=1, foo;a744=1, foo;a745=1, foo;a746=1, foo;a747=1, foo;a748=1, foo;a749=1, foo;a750=1, foo;a751=1, foo;a752=1, foo;a753=1, foo;a754=1, foo;a755=1, foo;a756=1, foo;a757=1, foo;a758=1, foo;a759=1, foo;a760=1, foo;a761=1, foo;a762=1, foo;a763=1, foo;a764=1, foo;a765=1, foo;a766=1, foo;a767=1, foo;a768=1, foo;a769=1, foo;a770=1, foo;a771=1, foo;a772=1, foo;a773=1, foo;a774=1, foo;a775=1, foo;a776=1, foo;a777=1, foo;a778=1, foo;a779=1, foo;a780=1, foo;a781=1, foo;a782=1, foo;a783=1, foo;a784=1, foo;a785=1, foo;a786=1, foo;a787=1, foo;a788=1, foo;a789=1, foo;a790=1, foo;a791=1, foo;a792=1, foo;a793=1, foo;a794=1, foo;a795=1, foo;a796=1, foo;a797=1, foo;a798=1, foo;a799=1, foo;a800=1, foo;a801=1, foo;a802=1, foo;a803=1, foo;a804=1, foo;a805=1, foo;a806=1, foo;a807=1, foo;a808=1, foo;a809=1, foo;a810=1, foo;a811=1, foo;a812=1, foo;a813=1, foo;a814=1, foo;a815=1, foo;a816=1, foo;a817=1, foo;a818=1, foo;a819=1, foo;a820=1, foo;a821=1, foo;a822=1, foo;a823=1, foo;a824=1, foo;a825=1, foo;a826=1, foo;a827=1, foo;a828=1, foo;a829=1, foo;a830=1, foo;a831=1, foo;a832=1, foo;a833=1, foo;a834=1, foo;a835=1, foo;a836=1, foo;a837=1, foo;a838=1, foo;a839=1, foo;a840=1, foo;a841=1, foo;a842=1, foo;a843=1, foo;a844=1, foo;a845=1, foo;a846=1, foo;a847=1, foo;a848=1, foo;a849=1, foo;a850=1, foo;a851=1, foo;a852=1, foo;a853=1, foo;a854=1, foo;a855=1, foo;a856=1, foo;a857=1, foo;a858=1, foo;a859=1, foo;a860=1, foo;a861=1, foo;a862=1, foo;a863=1, foo;a864=1, foo;a865=1, foo;a866=1, foo;a867=1, foo;a868=1, foo;a869=1, foo;a870=1, foo;a871=1, foo;a872=1, foo;a873=1, foo;a874=1, foo;a875=1, foo;a876=1, foo;a877=1, foo;a878=1, foo;a879=1, foo;a880=1, foo;a881=1, foo;a882=1, foo;a883=1, foo;a884=1, foo;a885=1, foo;a886=1, foo;a887=1, foo;a888=1, foo;a889=1, foo;a890=1, foo;a891=1, foo;a892=1, foo;a893=1, foo;a894=1, foo;a895=1, foo;a896=1, foo;a897=1, foo;a898=1, foo;a899=1, foo;a900=1, foo;a901=1, foo;a902=1, foo;a903=1, foo;a904=1, foo;a905=1, foo;a906=1, foo;a907=1, foo;a908=1, foo;a909=1, foo;a910=1, foo;a911=1, foo;a912=1, foo;a913=1, foo;a914=1, foo;a915=1, foo;a916=1, foo;a917=1, foo;a918=1, foo;a919=1, foo;a920=1, foo;a921=1, foo;a922=1, foo;a923=1, foo;a924=1, foo;a925=1, foo;a926=1, foo;a927=1, foo;a928=1, foo;a929=1, foo;a930=1, foo;a931=1, foo;a932=1, foo;a933=1, foo;a934=1, foo;a935=1, foo;a936=1, foo;a937=1, foo;a938=1, foo;a939=1, foo;a940=1, foo;a941=1, foo;a942=1, foo;a943=1, foo;a944=1, foo;a945=1, foo;a946=1, foo;a947=1, foo;a948=1, foo;a949=1, foo;a950=1, foo;a951=1, foo;a952=1, foo;a953=1, foo;a954=1, foo;a955=1, foo;a956=1, foo;a957=1, foo;a958=1, foo;a959=1, foo;a960=1, foo;a961=1, foo;a962=1, foo;a963=1, foo;a964=1, foo;a965=1, foo;a966=1, foo;a967=1, foo;a968=1, foo;a969=1, foo;a970=1, foo;a971=1, foo;a972=1, foo;a973=1, foo;a974=1, foo;a975=1, foo;a976=1, foo;a977=1, foo;a978=1, foo;a979=1, foo;a980=1, foo;a981=1, foo;a982=1, foo;a983=1, foo;a984=1, foo;a985=1, foo;a986=1, foo;a987=1, foo;a988=1, foo;a989=1, foo;a990=1, foo;a991=1, foo;a992=1, foo;a993=1, foo;a994=1, foo;a995=1, foo;a996=1, foo;a997=1, foo;a998=1, foo;a999=1, foo;a1000=1, foo;a1001=1, foo;a1002=1, foo;a1003=1, foo;a1004=1, foo;a1005=1, foo;a1006=1, foo;a1007=1, foo;a1008=1, foo;a1009=1, foo;a1010=1, foo;a1011=1, foo;a1012=1, foo;a1013=1, foo;a1014=1, foo;a1015=1, foo;a1016=1, foo;a1017=1, foo;a1018=1, foo;a1019=1, foo;a1020=1, foo;a1021=1, foo;a1022=1, foo;a1023=1
~~~

Result:
~~~
foo;a0=1, foo;a1=1, foo;a2=1, foo;a3=1, foo;a4=1, foo;a5=1, foo;a6=1, foo;a7=1, foo;a8=1, foo;a9=1, foo;a10=1, foo;a11=1, foo;a12=1, foo;a13=1, foo;a14=1, foo;a15=1, foo;a16=1, foo;a17=1, foo;a18=1, foo;a19=1, foo;a20=1, foo;a21=1, foo;a22=1, foo;a23=1, foo;a24=1, foo;a25=1, foo;a26=1, foo;a27=1, foo;a28=1, foo;a29=1, foo;a30=1, foo;a31=1, foo;a32=1, foo;a33=1, foo;a34=1, foo;a35=1, foo;a36=1, foo;a37=1, foo;a38=1, foo;a39=1, foo;a40=1, foo;a41=1, foo;a42=1, foo;a43=1, foo;a44=1, foo;a45=1, foo;a46=1, foo;a47=1, foo;a48=1, foo;a49=1, foo;a50=1, foo;a51=1, foo;a52=1, foo;a53=1, foo;a54=1, foo;a55=1, foo;a56=1, foo;a57=1, foo;a58=1, foo;a59=1, foo;a60=1, foo;a61=1, foo;a62=1, foo;a63=1, foo;a64=1, foo;a65=1, foo;a66=1, foo;a67=1, foo;a68=1, foo;a69=1, foo;a70=1, foo;a71=1, foo;a72=1, foo;a73=1, foo;a74=1, foo;a75=1, foo;a76=1, foo;a77=1, foo;a78=1, foo;a79=1, foo;a80=1, foo;a81=1, foo;a82=1, foo;a83=1, foo;a84=1, foo;a85=1, foo;a86=1, foo;a87=1, foo;a88=1, foo;a89=1, foo;a90=1, foo;a91=1, foo;a92=1, foo;a93=1, foo;a94=1, foo;a95=1, foo;a96=1, foo;a97=1, foo;a98=1, foo;a99=1, foo;a100=1, foo;a101=1, foo;a102=1, foo;a103=1, foo;a104=1, foo;a105=1, foo;a106=1, foo;a107=1, foo;a108=1, foo;a109=1, foo;a110=1, foo;a111=1, foo;a112=1, foo;a113=1, foo;a114=1, foo;a115=1, foo;a116=1, foo;a117=1, foo;a118=1, foo;a119=1, foo;a120=1, foo;a121=1, foo;a122=1, foo;a123=1, foo;a124=1, foo;a125=1, foo;a126=1, foo;a127=1, foo;a128=1, foo;a129=1, foo;a130=1, foo;a131=1, foo;a132=1, foo;a133=1, foo;a134=1, foo;a135=1, foo;a136=1, foo;a137=1, foo;a138=1, foo;a139=1, foo;a140=1, foo;a141=1, foo;a142=1, foo;a143=1, foo;a144=1, foo;a145=1, foo;a146=1, foo;a147=1, foo;a148=1, foo;a149=1, foo;a150=1, foo;a151=1, foo;a152=1, foo;a153=1, foo;a154=1, foo;a155=1, foo;a156=1, foo;a157=1, foo;a158=1, foo;a159=1, foo;a160=1, foo;a161=1, foo;a162=1, foo;a163=1, foo;a164=1, foo;a165=1, foo;a166=1, foo;a167=1, foo;a168=1, foo;a169=1, foo;a170=1, foo;a171=1, foo;a172=1, foo;a173=1, foo;a174=1, foo;a175=1, foo;a176=1, foo;a177=1, foo;a178=1, foo;a179=1, foo;a180=1, foo;a181=1, foo;a182=1, foo;a183=1, foo;a184=1, foo;a185=1, foo;a186=1, foo;a187=1, foo;a188=1, foo;a189=1, foo;a190=1, foo;a191=1, foo;a192=1, foo;a193=1, foo;a194=1, foo;a195=1, foo;a196=1, foo;a197=1, foo;a198=1, foo;a199=1, foo;a200=1, foo;a201=1, foo;a202=1, foo;a203=1, foo;a204=1, foo;a205=1, foo;a206=1, foo;a207=1, foo;a208=1, foo;a209=1, foo;a210=1, foo;a211=1, foo;a212=1, foo;a213=1, foo;a214=1, foo;a215=1, foo;a216=1, foo;a217=1, foo;a218=1, foo;a219=1, foo;a220=1, foo;a221=1, foo;a222=1, foo;a223=1, foo;a224=1, foo;a225=1, foo;a226=1, foo;a227=1, foo;a228=1, foo;a229=1, foo;a230=1, foo;a231=1, foo;a232=1, foo;a233=1, foo;a234=1, foo;a235=1, foo;a236=1, foo;a237=1, foo;a238=1, foo;a239=1, foo;a240=1, foo;a241=1, foo;a242=1, foo;a243=1, foo;a244=1, foo;a245=1, foo;a246=1, foo;a247=1, foo;a248=1, foo;a249=1, foo;a250=1, foo;a251=1, foo;a252=1, foo;a253=1, foo;a254=1, foo;a255=1, foo;a256=1, foo;a257=1, foo;a258=1, foo;a259=1, foo;a260=1, foo;a261=1, foo;a262=1, foo;a263=1, foo;a264=1, foo;a265=1, foo;a266=1, foo;a267=1, foo;a268=1, foo;a269=1, foo;a270=1, foo;a271=1, foo;a272=1, foo;a273=1, foo;a274=1, foo;a275=1, foo;a276=1, foo;a277=1, foo;a278=1, foo;a279=1, foo;a280=1, foo;a281=1, foo;a282=1, foo;a283=1, foo;a284=1, foo;a285=1, foo;a286=1, foo;a287=1, foo;a288=1, foo;a289=1, foo;a290=1, foo;a291=1, foo;a292=1, foo;a293=1, foo;a294=1, foo;a295=1, foo;a296=1, foo;a297=1, foo;a298=1, foo;a299=1, foo;a300=1, foo;a301=1, foo;a302=1, foo;a303=1, foo;a304=1, foo;a305=1, foo;a306=1, foo;a307=1, foo;a308=1, foo;a309=1, foo;a310=1, foo;a311=1, foo;a312=1, foo;a313=1, foo;a314=1, foo;a315=1, foo;a316=1, foo;a317=1, foo;a318=1, foo;a319=1, foo;a320=1, foo;a321=1, foo;a322=1, foo;a323=1, foo;a324=1, foo;a325=1, foo;a326=1, foo;a327=1, foo;a328=1, foo;a329=1, foo;a330=1, foo;a331=1, foo;a332=1, foo;a333=1, foo;a334=1, foo;a335=1, foo;a336=1, foo;a337=1, foo;a338=1, foo;a339=1, foo;a340=1, foo;a341=1, foo;a342=1, foo;a343=1, foo;a344=1, foo;a345=1, foo;a346=1, foo;a347=1, foo;a348=1, foo;a349=1, foo;a350=1, foo;a351=1, foo;a352=1, foo;a353=1, foo;a354=1, foo;a355=1, foo;a356=1, foo;a357=1, foo;a358=1, foo;a359=1, foo;a360=1, foo;a361=1, foo;a362=1, foo;a363=1, foo;a364=1, foo;a365=1, foo;a366=1, foo;a367=1, foo;a368=1, foo;a369=1, foo;a370=1, foo;a371=1, foo;a372=1, foo;a373=1, foo;a374=1, foo;a375=1, foo;a376=1, foo;a377=1, foo;a378=1, foo;a379=1, foo;a380=1, foo;a381=1, foo;a382=1, foo;a383=1, foo;a384=1, foo;a385=1, foo;a386=1, foo;a387=1, foo;a388=1, foo;a389=1, foo;a390=1, foo;a391=1, foo;a392=1, foo;a393=1, foo;a394=1, foo;a395=1, foo;a396=1, foo;a397=1, foo;a398=1, foo;a399=1, foo;a400=1, foo;a401=1, foo;a402=1, foo;a403=1, foo;a404=1, foo;a405=1, foo;a406=1, foo;a407=1, foo;a408=1, foo;a409=1, foo;a410=1, foo;a411=1, foo;a412=1, foo;a413=1, foo;a414=1, foo;a415=1, foo;a416=1, foo;a417=1, foo;a418=1, foo;a419=1, foo;a420=1, foo;a421=1, foo;a422=1, foo;a423=1, foo;a424=1, foo;a425=1, foo;a426=1, foo;a427=1, foo;a428=1, foo;a429=1, foo;a430=1, foo;a431=1, foo;a432=1, foo;a433=1, foo;a434=1, foo;a435=1, foo;a436=1, foo;a437=1, foo;a438=1, foo;a439=1, foo;a440=1, foo;a441=1, foo;a442=1, foo;a443=1, foo;a444=1, foo;a445=1, foo;a446=1, foo;a447=1, foo;a448=1, foo;a449=1, foo;a450=1, foo;a451=1, foo;a452=1, foo;a453=1, foo;a454=1, foo;a455=1, foo;a456=1, foo;a457=1, foo;a458=1, foo;a459=1, foo;a460=1, foo;a461=1, foo;a462=1, foo;a463=1, foo;a464=1, foo;a465=1, foo;a466=1, foo;a467=1, foo;a468=1, foo;a469=1, foo;a470=1, foo;a471=1, foo;a472=1, foo;a473=1, foo;a474=1, foo;a475=1, foo;a476=1, foo;a477=1, foo;a478=1, foo;a479=1, foo;a480=1, foo;a481=1, foo;a482=1, foo;a483=1, foo;a484=1, foo;a485=1, foo;a486=1, foo;a487=1, foo;a488=1, foo;a489=1, foo;a490=1, foo;a491=1, foo;a492=1, foo;a493=1, foo;a494=1, foo;a495=1, foo;a496=1, foo;a497=1, foo;a498=1, foo;a499=1, foo;a500=1, foo;a501=1, foo;a502=1, foo;a503=1, foo;a504=1, foo;a505=1, foo;a506=1, foo;a507=1, foo;a508=1, foo;a509=1, foo;a510=1, foo;a511=1, foo;a512=1, foo;a513=1, foo;a514=1, foo;a515=1, foo;a516=1, foo;a517=1, foo;a518=1, foo;a519=1, foo;a520=1, foo;a521=1, foo;a522=1, foo;a523=1, foo;a524=1, foo;a525=1, foo;a526=1, foo;a527=1, foo;a528=1, foo;a529=1, foo;a530=1, foo;a531=1, foo;a532=1, foo;a533=1, foo;a534=1, foo;a535=1, foo;a536=1, foo;a537=1, foo;a538=1, foo;a539=1, foo;a540=1, foo;a541=1, foo;a542=1, foo;a543=1, foo;a544=1, foo;a545=1, foo;a546=1, foo;a547=1, foo;a548=1, foo;a549=1, foo;a550=1, foo;a551=1, foo;a552=1, foo;a553=1, foo;a554=1, foo;a555=1, foo;a556=1, foo;a557=1, foo;a558=1, foo;a559=1, foo;a560=1, foo;a561=1, foo;a562=1, foo;a563=1, foo;a564=1, foo;a565=1, foo;a566=1, foo;a567=1, foo;a568=1, foo;a569=1, foo;a570=1, foo;a571=1, foo;a572=1, foo;a573=1, foo;a574=1, foo;a575=1, foo;a576=1, foo;a577=1, foo;a578=1, foo;a579=1, foo;a580=1, foo;a581=1, foo;a582=1, foo;a583=1, foo;a584=1, foo;a585=1, foo;a586=1, foo;a587=1, foo;a588=1, foo;a589=1, foo;a590=1, foo;a591=1, foo;a592=1, foo;a593=1, foo;a594=1, foo;a595=1, foo;a596=1, foo;a597=1, foo;a598=1, foo;a599=1, foo;a600=1, foo;a601=1, foo;a602=1, foo;a603=1, foo;a604=1, foo;a605=1, foo;a606=1, foo;a607=1, foo;a608=1, foo;a609=1, foo;a610=1, foo;a611=1, foo;a612=1, foo;a613=1, foo;a614=1, foo;a615=1, foo;a616=1, foo;a617=1, foo;a618=1, foo;a619=1, foo;a620=1, foo;a621=1, foo;a622=1, foo;a623=1, foo;a624=1, foo;a625=1, foo;a626=1, foo;a627=1, foo;a628=1, foo;a629=1, foo;a630=1, foo;a631=1, foo;a632=1, foo;a633=1, foo;a634=1, foo;a635=1, foo;a636=1, foo;a637=1, foo;a638=1, foo;a639=1, foo;a640=1, foo;a641=1, foo;a642=1, foo;a643=1, foo;a644=1, foo;a645=1, foo;a646=1, foo;a647=1, foo;a648=1, foo;a649=1, foo;a650=1, foo;a651=1, foo;a652=1, foo;a653=1, foo;a654=1, foo;a655=1, foo;a656=1, foo;a657=1, foo;a658=1, foo;a659=1, foo;a660=1, foo;a661=1, foo;a662=1, foo;a663=1, foo;a664=1, foo;a665=1, foo;a666=1, foo;a667=1, foo;a668=1, foo;a669=1, foo;a670=1, foo;a671=1, foo;a672=1, foo;a673=1, foo;a674=1, foo;a675=1, foo;a676=1, foo;a677=1, foo;a678=1, foo;a679=1, foo;a680=1, foo;a681=1, foo;a682=1, foo;a683=1, foo;a684=1, foo;a685=1, foo;a686=1, foo;a687=1, foo;a688=1, foo;a689=1, foo;a690=1, foo;a691=1, foo;a692=1, foo;a693=1, foo;a694=1, foo;a695=1, foo;a696=1, foo;a697=1, foo;a698=1, foo;a699=1, foo;a700=1, foo;a701=1, foo;a702=1, foo;a703=1, foo;a704=1, foo;a705=1, foo;a706=1, foo;a707=1, foo;a708=1, foo;a709=1, foo;a710=1, foo;a711=1, foo;a712=1, foo;a713=1, foo;a714=1, foo;a715=1, foo;a716=1, foo;a717=1, foo;a718=1, foo;a719=1, foo;a720=1, foo;a721=1, foo;a722=1, foo;a723=1, foo;a724=1, foo;a725=1, foo;a726=1, foo;a727=1, foo;a728=1, foo;a729=1, foo;a730=1, foo;a731=1, foo;a732=1, foo;a733=1, foo;a734=1, foo;a735=1, foo;a736=1, foo;a737=1, foo;a738=1, foo;a739=1, foo;a740=1, foo;a741=1, foo;a742=1, foo;a743=1, foo;a744=1, foo;a745=1, foo;a746=1, foo;a747=1, foo;a748=1, foo;a749=1, foo;a750=1, foo;a751=1, foo;a752=1, foo;a753=1, foo;a754=1, foo;a755=1, foo;a756=1, foo;a757=1, foo;a758=1, foo;a759=1, foo;a760=1, foo;a761=1, foo;a762=1, foo;a763=1, foo;a764=1, foo;a765=1, foo;a766=1, foo;a767=1, foo;a768=1, foo;a769=1, foo;a770=1, foo;a771=1, foo;a772=1, foo;a773=1, foo;a774=1, foo;a775=1, foo;a776=1, foo;a777=1, foo;a778=1, foo;a779=1, foo;a780=1, foo;a781=1, foo;a782=1, foo;a783=1, foo;a784=1, foo;a785=1, foo;a786=1, foo;a787=1, foo;a788=1, foo;a789=1, foo;a790=1, foo;a791=1, foo;a792=1, foo;a793=1, foo;a794=1, foo;a795=1, foo;a796=1, foo;a797=1, foo;a798=1, foo;a799=1, foo;a800=1, foo;a801=1, foo;a802=1, foo;a803=1, foo;a804=1, foo;a805=1, foo;a806=1, foo;a807=1, foo;a808=1, foo;a809=1, foo;a810=1, foo;a811=1, foo;a812=1, foo;a813=1, foo;a814=1, foo;a815=1, foo;a816=1, foo;a817=1, foo;a818=1, foo;a819=1, foo;a820=1, foo;a821=1, foo;a822=1, foo;a823=1, foo;a824=1, foo;a825=1, foo;a826=1, foo;a827=1, foo;a828=1, foo;a829=1, foo;a830=1, foo;a831=1, foo;a832=1, foo;a833=1, foo;a834=1, foo;a835=1, foo;a836=1, foo;a837=1, foo;a838=1, foo;a839=1, foo;a840=1, foo;a841=1, foo;a842=1, foo;a843=1, foo;a844=1, foo;a845=1, foo;a846=1, foo;a847=1, foo;a848=1, foo;a849=1, foo;a850=1, foo;a851=1, foo;a852=1, foo;a853=1, foo;a854=1, foo;a855=1, foo;a856=1, foo;a857=1, foo;a858=1, foo;a859=1, foo;a860=1, foo;a861=1, foo;a862=1, foo;a863=1, foo;a864=1, foo;a865=1, foo;a866=1, foo;a867=1, foo;a868=1, foo;a869=1, foo;a870=1, foo;a871=1, foo;a872=1, foo;a873=1, foo;a874=1, foo;a875=1, foo;a876=1, foo;a877=1, foo;a878=1, foo;a879=1, foo;a880=1, foo;a881=1, foo;a882=1, foo;a883=1, foo;a884=1, foo;a885=1, foo;a886=1, foo;a887=1, foo;a888=1, foo;a889=1, foo;a890=1, foo;a891=1, foo;a892=1, foo;a893=1, foo;a894=1, foo;a895=1, foo;a896=1, foo;a897=1, foo;a898=1, foo;a899=1, foo;a900=1, foo;a901=1, foo;a902=1, foo;a903=1, foo;a904=1, foo;a905=1, foo;a906=1, foo;a907=1, foo;a908=1, foo;a909=1, foo;a910=1, foo;a911=1, foo;a912=1, foo;a913=1, foo;a914=1, foo;a915=1, foo;a916=1, foo;a917=1, foo;a918=1, foo;a919=1, foo;a920=1, foo;a921=1, foo;a922=1, foo;a923=1, foo;a924=1, foo;a925=1, foo;a926=1, foo;a927=1, foo;a928=1, foo;a929=1, foo;a930=1, foo;a931=1, foo;a932=1, foo;a933=1, foo;a934=1, foo;a935=1, foo;a936=1, foo;a937=1, foo;a938=1, foo;a939=1, foo;a940=1, foo;a941=1, foo;a942=1, foo;a943=1, foo;a944=1, foo;a945=1, foo;a946=1, foo;a947=1, foo;a948=1, foo;a949=1, foo;a950=1, foo;a951=1, foo;a952=1, foo;a953=1, foo;a954=1, foo;a955=1, foo;a956=1, foo;a957=1, foo;a958=1, foo;a959=1, foo;a960=1, foo;a961=1, foo;a962=1, foo;a963=1, foo;a964=1, foo;a965=1, foo;a966=1, foo;a967=1, foo;a968=1, foo;a969=1, foo;a970=1, foo;a971=1, foo;a972=1, foo;a973=1, foo;a974=1, foo;a975=1, foo;a976=1, foo;a977=1, foo;a978=1, foo;a979=1, foo;a980=1, foo;a981=1, foo;a982=1, foo;a983=1, foo;a984=1, foo;a985=1, foo;a986=1, foo;a987=1, foo;a988=1, foo;a989=1, foo;a990=1, foo;a991=1, foo;a992=1, foo;a993=1, foo;a994=1, foo;a995=1, foo;a996=1, foo;a997=1, foo;a998=1, foo;a999=1, foo;a1000=1, foo;a1001=1, foo;a1002=1, foo;a1003=1, foo;a1004=1, foo;a1005=1, foo;a1006=1, foo;a1007=1, foo;a1008=1, foo;a1009=1, foo;a1010=1, foo;a1011=1, foo;a1012=1, foo;a1013=1, foo;a1014=1, foo;a1015=1, foo;a1016=1, foo;a1017=1, foo;a1018=1, foo;a1019=1, foo;a1020=1, foo;a1021=1, foo;a1022=1, foo;a1023=1
~~~

### large params

Input:
~~~
foo;a0=1;a1=1;a2=1;a3=1;a4=1;a5=1;a6=1;a7=1;a8=1;a9=1;a10=1;a11=1;a12=1;a13=1;a14=1;a15=1;a16=1;a17=1;a18=1;a19=1;a20=1;a21=1;a22=1;a23=1;a24=1;a25=1;a26=1;a27=1;a28=1;a29=1;a30=1;a31=1;a32=1;a33=1;a34=1;a35=1;a36=1;a37=1;a38=1;a39=1;a40=1;a41=1;a42=1;a43=1;a44=1;a45=1;a46=1;a47=1;a48=1;a49=1;a50=1;a51=1;a52=1;a53=1;a54=1;a55=1;a56=1;a57=1;a58=1;a59=1;a60=1;a61=1;a62=1;a63=1;a64=1;a65=1;a66=1;a67=1;a68=1;a69=1;a70=1;a71=1;a72=1;a73=1;a74=1;a75=1;a76=1;a77=1;a78=1;a79=1;a80=1;a81=1;a82=1;a83=1;a84=1;a85=1;a86=1;a87=1;a88=1;a89=1;a90=1;a91=1;a92=1;a93=1;a94=1;a95=1;a96=1;a97=1;a98=1;a99=1;a100=1;a101=1;a102=1;a103=1;a104=1;a105=1;a106=1;a107=1;a108=1;a109=1;a110=1;a111=1;a112=1;a113=1;a114=1;a115=1;a116=1;a117=1;a118=1;a119=1;a120=1;a121=1;a122=1;a123=1;a124=1;a125=1;a126=1;a127=1;a128=1;a129=1;a130=1;a131=1;a132=1;a133=1;a134=1;a135=1;a136=1;a137=1;a138=1;a139=1;a140=1;a141=1;a142=1;a143=1;a144=1;a145=1;a146=1;a147=1;a148=1;a149=1;a150=1;a151=1;a152=1;a153=1;a154=1;a155=1;a156=1;a157=1;a158=1;a159=1;a160=1;a161=1;a162=1;a163=1;a164=1;a165=1;a166=1;a167=1;a168=1;a169=1;a170=1;a171=1;a172=1;a173=1;a174=1;a175=1;a176=1;a177=1;a178=1;a179=1;a180=1;a181=1;a182=1;a183=1;a184=1;a185=1;a186=1;a187=1;a188=1;a189=1;a190=1;a191=1;a192=1;a193=1;a194=1;a195=1;a196=1;a197=1;a198=1;a199=1;a200=1;a201=1;a202=1;a203=1;a204=1;a205=1;a206=1;a207=1;a208=1;a209=1;a210=1;a211=1;a212=1;a213=1;a214=1;a215=1;a216=1;a217=1;a218=1;a219=1;a220=1;a221=1;a222=1;a223=1;a224=1;a225=1;a226=1;a227=1;a228=1;a229=1;a230=1;a231=1;a232=1;a233=1;a234=1;a235=1;a236=1;a237=1;a238=1;a239=1;a240=1;a241=1;a242=1;a243=1;a244=1;a245=1;a246=1;a247=1;a248=1;a249=1;a250=1;a251=1;a252=1;a253=1;a254=1;a255=1
~~~

Result:
~~~
foo;a0=1;a1=1;a2=1;a3=1;a4=1;a5=1;a6=1;a7=1;a8=1;a9=1;a10=1;a11=1;a12=1;a13=1;a14=1;a15=1;a16=1;a17=1;a18=1;a19=1;a20=1;a21=1;a22=1;a23=1;a24=1;a25=1;a26=1;a27=1;a28=1;a29=1;a30=1;a31=1;a32=1;a33=1;a34=1;a35=1;a36=1;a37=1;a38=1;a39=1;a40=1;a41=1;a42=1;a43=1;a44=1;a45=1;a46=1;a47=1;a48=1;a49=1;a50=1;a51=1;a52=1;a53=1;a54=1;a55=1;a56=1;a57=1;a58=1;a59=1;a60=1;a61=1;a62=1;a63=1;a64=1;a65=1;a66=1;a67=1;a68=1;a69=1;a70=1;a71=1;a72=1;a73=1;a74=1;a75=1;a76=1;a77=1;a78=1;a79=1;a80=1;a81=1;a82=1;a83=1;a84=1;a85=1;a86=1;a87=1;a88=1;a89=1;a90=1;a91=1;a92=1;a93=1;a94=1;a95=1;a96=1;a97=1;a98=1;a99=1;a100=1;a101=1;a102=1;a103=1;a104=1;a105=1;a106=1;a107=1;a108=1;a109=1;a110=1;a111=1;a112=1;a113=1;a114=1;a115=1;a116=1;a117=1;a118=1;a119=1;a120=1;a121=1;a122=1;a123=1;a124=1;a125=1;a126=1;a127=1;a128=1;a129=1;a130=1;a131=1;a132=1;a133=1;a134=1;a135=1;a136=1;a137=1;a138=1;a139=1;a140=1;a141=1;a142=1;a143=1;a144=1;a145=1;a146=1;a147=1;a148=1;a149=1;a150=1;a151=1;a152=1;a153=1;a154=1;a155=1;a156=1;a157=1;a158=1;a159=1;a160=1;a161=1;a162=1;a163=1;a164=1;a165=1;a166=1;a167=1;a168=1;a169=1;a170=1;a171=1;a172=1;a173=1;a174=1;a175=1;a176=1;a177=1;a178=1;a179=1;a180=1;a181=1;a182=1;a183=1;a184=1;a185=1;a186=1;a187=1;a188=1;a189=1;a190=1;a191=1;a192=1;a193=1;a194=1;a195=1;a196=1;a197=1;a198=1;a199=1;a200=1;a201=1;a202=1;a203=1;a204=1;a205=1;a206=1;a207=1;a208=1;a209=1;a210=1;a211=1;a212=1;a213=1;a214=1;a215=1;a216=1;a217=1;a218=1;a219=1;a220=1;a221=1;a222=1;a223=1;a224=1;a225=1;a226=1;a227=1;a228=1;a229=1;a230=1;a231=1;a232=1;a233=1;a234=1;a235=1;a236=1;a237=1;a238=1;a239=1;a240=1;a241=1;a242=1;a243=1;a244=1;a245=1;a246=1;a247=1;a248=1;a249=1;a250=1;a251=1;a252=1;a253=1;a254=1;a255=1
~~~

### large param key

Input:
~~~
foo;aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=1
~~~

Result:
~~~
foo;aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=1
~~~

### large string

Input:
~~~
"================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================"
~~~

Result:
~~~
"================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================"
~~~

### large escaped string

Input:
~~~
"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\""
~~~

Result:
~~~
"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\""
~~~

### large token

Input:
~~~
aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
~~~

Result:
~~~
aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
~~~


## list


### basic list

Input:
~~~
1, 42
~~~

Result:
~~~
1, 42
~~~

### empty list

Input:
~~~

~~~

Result:
~~~

~~~

### leading SP list

Input:
~~~
  42, 43
~~~

Result:
~~~
42, 43
~~~

### single item list

Input:
~~~
42
~~~

Result:
~~~
42
~~~

### no whitespace list

Input:
~~~
1,42
~~~

Result:
~~~
1, 42
~~~

### extra whitespace list

Input:
~~~
1 , 42
~~~

Result:
~~~
1, 42
~~~

### tab separated list

Input:
~~~
1	,	42
~~~

Result:
~~~
1, 42
~~~

### two line list

Input:
~~~
1
42
~~~

Result:
~~~
1, 42
~~~

### trailing comma list

Input:
~~~
1, 42,
~~~

Expects Parse Error
~~~
>>1, 42,<<
  ------^ Found trailing COMMA in List
~~~


### empty item list

Input:
~~~
1,,42
~~~

Expects Parse Error
~~~
>>1,,42<<
  --^ (0x2c) Unexpected start character in Bare Item: ',' (\u002c)
~~~


### empty item list (multiple field lines)

Input:
~~~
1

42
~~~

Expects Parse Error
~~~
>>1,,42<<
  --^ (0x2c) Unexpected start character in Bare Item: ',' (\u002c)
~~~



## listlist


### basic list of lists

Input:
~~~
(1 2), (42 43)
~~~

Result:
~~~
(1 2), (42 43)
~~~

### single item list of lists

Input:
~~~
(42)
~~~

Result:
~~~
(42)
~~~

### empty item list of lists

Input:
~~~
()
~~~

Result:
~~~
()
~~~

### empty middle item list of lists

Input:
~~~
(1),(),(42)
~~~

Result:
~~~
(1), (), (42)
~~~

### extra whitespace list of lists

Input:
~~~
(  1  42  )
~~~

Result:
~~~
(1 42)
~~~

### wrong whitespace list of lists

Input:
~~~
(1	 42)
~~~

Expects Parse Error
~~~
>>(1	 42)<<
  --^ (0x09) Expected SP or ')' in Inner List, got: HTAB (\u0009)
~~~


### no trailing parenthesis list of lists

Input:
~~~
(1 42
~~~

Expects Parse Error
~~~
>>(1 42<<
  -----^ Missing data in Inner List
~~~


### no trailing parenthesis middle list of lists

Input:
~~~
(1 2, (42 43)
~~~

Expects Parse Error
~~~
>>(1 2, (42 43)<<
  ----^ (0x2c) Expected SP or ')' in Inner List, got: ',' (\u002c)
~~~


### no spaces in inner-list

Input:
~~~
(abc"def"?0123*dXZ3*xyz)
~~~

Expects Parse Error
~~~
>>(abc"def"?0123*dXZ3*xyz)<<
  ----^ (0x22) Expected SP or ')' in Inner List, got: '"' (\u0022)
~~~


### no closing parenthesis

Input:
~~~
(
~~~

Expects Parse Error
~~~
>>(<<
  -^ Inner List must end with ')': 
~~~



## number


### basic integer

Input:
~~~
42
~~~

Result:
~~~
42
~~~

### zero integer

Input:
~~~
0
~~~

Result:
~~~
0
~~~

### negative zero

Input:
~~~
-0
~~~

Result:
~~~
0
~~~

### double negative zero

Input:
~~~
--0
~~~

Expects Parse Error
~~~
>>--0<<
  -^ (0x2d) Illegal start for Integer or Decimal: '-0'
~~~


### negative integer

Input:
~~~
-42
~~~

Result:
~~~
-42
~~~

### leading 0 integer

Input:
~~~
042
~~~

Result:
~~~
42
~~~

### leading 0 negative integer

Input:
~~~
-042
~~~

Result:
~~~
-42
~~~

### leading 0 zero

Input:
~~~
00
~~~

Result:
~~~
0
~~~

### comma

Input:
~~~
2,3
~~~

Expects Parse Error
~~~
>>2,3<<
  -^ (0x2c) Extra characters in string parsed as Item
~~~


### negative non-DIGIT first character

Input:
~~~
-a23
~~~

Expects Parse Error
~~~
>>-a23<<
  -^ (0x61) Illegal start for Integer or Decimal: 'a23'
~~~


### sign out of place

Input:
~~~
4-2
~~~

Expects Parse Error
~~~
>>4-2<<
  -^ (0x2d) Extra characters in string parsed as Item
~~~


### whitespace after sign

Input:
~~~
- 42
~~~

Expects Parse Error
~~~
>>- 42<<
  -^ (0x20) Illegal start for Integer or Decimal: ' 42'
~~~


### long integer

Input:
~~~
123456789012345
~~~

Result:
~~~
123456789012345
~~~

### long negative integer

Input:
~~~
-123456789012345
~~~

Result:
~~~
-123456789012345
~~~

### too long integer

Input:
~~~
1234567890123456
~~~

Expects Parse Error
~~~
>>1234567890123456<<
  ---------------^ (0x36) Integer too long: 16 characters
~~~


### negative too long integer

Input:
~~~
-1234567890123456
~~~

Expects Parse Error
~~~
>>-1234567890123456<<
  ----------------^ (0x36) Integer too long: 16 characters
~~~


### simple decimal

Input:
~~~
1.23
~~~

Result:
~~~
1.23
~~~

### negative decimal

Input:
~~~
-1.23
~~~

Result:
~~~
-1.23
~~~

### decimal, whitespace after decimal

Input:
~~~
1. 23
~~~

Expects Parse Error
~~~
>>1. 23<<
  -^ (0x2e) Decimal must not end in '.'
~~~


### decimal, whitespace before decimal

Input:
~~~
1 .23
~~~

Expects Parse Error
~~~
>>1 .23<<
  --^ (0x2e) Extra characters in string parsed as Item
~~~


### negative decimal, whitespace after sign

Input:
~~~
- 1.23
~~~

Expects Parse Error
~~~
>>- 1.23<<
  -^ (0x20) Illegal start for Integer or Decimal: ' 1.23'
~~~


### tricky precision decimal

Input:
~~~
123456789012.1
~~~

Result:
~~~
123456789012.1
~~~

### double decimal decimal

Input:
~~~
1.5.4
~~~

Expects Parse Error
~~~
>>1.5.4<<
  ---^ (0x2e) Extra characters in string parsed as Item
~~~


### adjacent double decimal decimal

Input:
~~~
1..4
~~~

Expects Parse Error
~~~
>>1..4<<
  -^ (0x2e) Decimal must not end in '.'
~~~


### decimal with three fractional digits

Input:
~~~
1.123
~~~

Result:
~~~
1.123
~~~

### negative decimal with three fractional digits

Input:
~~~
-1.123
~~~

Result:
~~~
-1.123
~~~

### decimal with four fractional digits

Input:
~~~
1.1234
~~~

Expects Parse Error
~~~
>>1.1234<<
  -----^ (0x34) Maximum number of fractional digits is 3, found: 4, in: 1.1234
~~~


### negative decimal with four fractional digits

Input:
~~~
-1.1234
~~~

Expects Parse Error
~~~
>>-1.1234<<
  ------^ (0x34) Maximum number of fractional digits is 3, found: 4, in: 1.1234
~~~


### decimal with thirteen integer digits

Input:
~~~
1234567890123.0
~~~

Expects Parse Error
~~~
>>1234567890123.0<<
  -------------^ (0x2e) Illegal position for decimal point in Decimal after '1234567890123'
~~~


### negative decimal with thirteen integer digits

Input:
~~~
-1234567890123.0
~~~

Expects Parse Error
~~~
>>-1234567890123.0<<
  --------------^ (0x2e) Illegal position for decimal point in Decimal after '1234567890123'
~~~


### decimal with 1 significant digit and 1 insignificant digit

Input:
~~~
1.20
~~~

Result:
~~~
1.2
~~~

### decimal with 1 significant digit and 2 insignificant digits

Input:
~~~
1.200
~~~

Result:
~~~
1.2
~~~

### decimal with 2 significant digits and 1 insignificant digit

Input:
~~~
1.230
~~~

Result:
~~~
1.23
~~~


## number-generated


### 1 digits of zero

Input:
~~~
0
~~~

Result:
~~~
0
~~~

### 1 digit small integer

Input:
~~~
1
~~~

Result:
~~~
1
~~~

### 1 digit large integer

Input:
~~~
9
~~~

Result:
~~~
9
~~~

### 2 digits of zero

Input:
~~~
00
~~~

Result:
~~~
0
~~~

### 2 digit small integer

Input:
~~~
11
~~~

Result:
~~~
11
~~~

### 2 digit large integer

Input:
~~~
99
~~~

Result:
~~~
99
~~~

### 3 digits of zero

Input:
~~~
000
~~~

Result:
~~~
0
~~~

### 3 digit small integer

Input:
~~~
111
~~~

Result:
~~~
111
~~~

### 3 digit large integer

Input:
~~~
999
~~~

Result:
~~~
999
~~~

### 4 digits of zero

Input:
~~~
0000
~~~

Result:
~~~
0
~~~

### 4 digit small integer

Input:
~~~
1111
~~~

Result:
~~~
1111
~~~

### 4 digit large integer

Input:
~~~
9999
~~~

Result:
~~~
9999
~~~

### 5 digits of zero

Input:
~~~
00000
~~~

Result:
~~~
0
~~~

### 5 digit small integer

Input:
~~~
11111
~~~

Result:
~~~
11111
~~~

### 5 digit large integer

Input:
~~~
99999
~~~

Result:
~~~
99999
~~~

### 6 digits of zero

Input:
~~~
000000
~~~

Result:
~~~
0
~~~

### 6 digit small integer

Input:
~~~
111111
~~~

Result:
~~~
111111
~~~

### 6 digit large integer

Input:
~~~
999999
~~~

Result:
~~~
999999
~~~

### 7 digits of zero

Input:
~~~
0000000
~~~

Result:
~~~
0
~~~

### 7 digit small integer

Input:
~~~
1111111
~~~

Result:
~~~
1111111
~~~

### 7 digit large integer

Input:
~~~
9999999
~~~

Result:
~~~
9999999
~~~

### 8 digits of zero

Input:
~~~
00000000
~~~

Result:
~~~
0
~~~

### 8 digit small integer

Input:
~~~
11111111
~~~

Result:
~~~
11111111
~~~

### 8 digit large integer

Input:
~~~
99999999
~~~

Result:
~~~
99999999
~~~

### 9 digits of zero

Input:
~~~
000000000
~~~

Result:
~~~
0
~~~

### 9 digit small integer

Input:
~~~
111111111
~~~

Result:
~~~
111111111
~~~

### 9 digit large integer

Input:
~~~
999999999
~~~

Result:
~~~
999999999
~~~

### 10 digits of zero

Input:
~~~
0000000000
~~~

Result:
~~~
0
~~~

### 10 digit small integer

Input:
~~~
1111111111
~~~

Result:
~~~
1111111111
~~~

### 10 digit large integer

Input:
~~~
9999999999
~~~

Result:
~~~
9999999999
~~~

### 11 digits of zero

Input:
~~~
00000000000
~~~

Result:
~~~
0
~~~

### 11 digit small integer

Input:
~~~
11111111111
~~~

Result:
~~~
11111111111
~~~

### 11 digit large integer

Input:
~~~
99999999999
~~~

Result:
~~~
99999999999
~~~

### 12 digits of zero

Input:
~~~
000000000000
~~~

Result:
~~~
0
~~~

### 12 digit small integer

Input:
~~~
111111111111
~~~

Result:
~~~
111111111111
~~~

### 12 digit large integer

Input:
~~~
999999999999
~~~

Result:
~~~
999999999999
~~~

### 13 digits of zero

Input:
~~~
0000000000000
~~~

Result:
~~~
0
~~~

### 13 digit small integer

Input:
~~~
1111111111111
~~~

Result:
~~~
1111111111111
~~~

### 13 digit large integer

Input:
~~~
9999999999999
~~~

Result:
~~~
9999999999999
~~~

### 14 digits of zero

Input:
~~~
00000000000000
~~~

Result:
~~~
0
~~~

### 14 digit small integer

Input:
~~~
11111111111111
~~~

Result:
~~~
11111111111111
~~~

### 14 digit large integer

Input:
~~~
99999999999999
~~~

Result:
~~~
99999999999999
~~~

### 15 digits of zero

Input:
~~~
000000000000000
~~~

Result:
~~~
0
~~~

### 15 digit small integer

Input:
~~~
111111111111111
~~~

Result:
~~~
111111111111111
~~~

### 15 digit large integer

Input:
~~~
999999999999999
~~~

Result:
~~~
999999999999999
~~~

### 2 digit 0, 1 fractional small decimal

Input:
~~~
0.1
~~~

Result:
~~~
0.1
~~~

### 2 digit, 1 fractional 0 decimal

Input:
~~~
1.0
~~~

Result:
~~~
1.0
~~~

### 2 digit, 1 fractional small decimal

Input:
~~~
1.1
~~~

Result:
~~~
1.1
~~~

### 2 digit, 1 fractional large decimal

Input:
~~~
9.9
~~~

Result:
~~~
9.9
~~~

### 3 digit 0, 2 fractional small decimal

Input:
~~~
0.11
~~~

Result:
~~~
0.11
~~~

### 3 digit, 2 fractional 0 decimal

Input:
~~~
1.00
~~~

Result:
~~~
1.0
~~~

### 3 digit, 2 fractional small decimal

Input:
~~~
1.11
~~~

Result:
~~~
1.11
~~~

### 3 digit, 2 fractional large decimal

Input:
~~~
9.99
~~~

Result:
~~~
9.99
~~~

### 4 digit 0, 3 fractional small decimal

Input:
~~~
0.111
~~~

Result:
~~~
0.111
~~~

### 4 digit, 3 fractional 0 decimal

Input:
~~~
1.000
~~~

Result:
~~~
1.0
~~~

### 4 digit, 3 fractional small decimal

Input:
~~~
1.111
~~~

Result:
~~~
1.111
~~~

### 4 digit, 3 fractional large decimal

Input:
~~~
9.999
~~~

Result:
~~~
9.999
~~~

### 3 digit 0, 1 fractional small decimal

Input:
~~~
00.1
~~~

Result:
~~~
0.1
~~~

### 3 digit, 1 fractional 0 decimal

Input:
~~~
11.0
~~~

Result:
~~~
11.0
~~~

### 3 digit, 1 fractional small decimal

Input:
~~~
11.1
~~~

Result:
~~~
11.1
~~~

### 3 digit, 1 fractional large decimal

Input:
~~~
99.9
~~~

Result:
~~~
99.9
~~~

### 4 digit 0, 2 fractional small decimal

Input:
~~~
00.11
~~~

Result:
~~~
0.11
~~~

### 4 digit, 2 fractional 0 decimal

Input:
~~~
11.00
~~~

Result:
~~~
11.0
~~~

### 4 digit, 2 fractional small decimal

Input:
~~~
11.11
~~~

Result:
~~~
11.11
~~~

### 4 digit, 2 fractional large decimal

Input:
~~~
99.99
~~~

Result:
~~~
99.99
~~~

### 5 digit 0, 3 fractional small decimal

Input:
~~~
00.111
~~~

Result:
~~~
0.111
~~~

### 5 digit, 3 fractional 0 decimal

Input:
~~~
11.000
~~~

Result:
~~~
11.0
~~~

### 5 digit, 3 fractional small decimal

Input:
~~~
11.111
~~~

Result:
~~~
11.111
~~~

### 5 digit, 3 fractional large decimal

Input:
~~~
99.999
~~~

Result:
~~~
99.999
~~~

### 4 digit 0, 1 fractional small decimal

Input:
~~~
000.1
~~~

Result:
~~~
0.1
~~~

### 4 digit, 1 fractional 0 decimal

Input:
~~~
111.0
~~~

Result:
~~~
111.0
~~~

### 4 digit, 1 fractional small decimal

Input:
~~~
111.1
~~~

Result:
~~~
111.1
~~~

### 4 digit, 1 fractional large decimal

Input:
~~~
999.9
~~~

Result:
~~~
999.9
~~~

### 5 digit 0, 2 fractional small decimal

Input:
~~~
000.11
~~~

Result:
~~~
0.11
~~~

### 5 digit, 2 fractional 0 decimal

Input:
~~~
111.00
~~~

Result:
~~~
111.0
~~~

### 5 digit, 2 fractional small decimal

Input:
~~~
111.11
~~~

Result:
~~~
111.11
~~~

### 5 digit, 2 fractional large decimal

Input:
~~~
999.99
~~~

Result:
~~~
999.99
~~~

### 6 digit 0, 3 fractional small decimal

Input:
~~~
000.111
~~~

Result:
~~~
0.111
~~~

### 6 digit, 3 fractional 0 decimal

Input:
~~~
111.000
~~~

Result:
~~~
111.0
~~~

### 6 digit, 3 fractional small decimal

Input:
~~~
111.111
~~~

Result:
~~~
111.111
~~~

### 6 digit, 3 fractional large decimal

Input:
~~~
999.999
~~~

Result:
~~~
999.999
~~~

### 5 digit 0, 1 fractional small decimal

Input:
~~~
0000.1
~~~

Result:
~~~
0.1
~~~

### 5 digit, 1 fractional 0 decimal

Input:
~~~
1111.0
~~~

Result:
~~~
1111.0
~~~

### 5 digit, 1 fractional small decimal

Input:
~~~
1111.1
~~~

Result:
~~~
1111.1
~~~

### 5 digit, 1 fractional large decimal

Input:
~~~
9999.9
~~~

Result:
~~~
9999.9
~~~

### 6 digit 0, 2 fractional small decimal

Input:
~~~
0000.11
~~~

Result:
~~~
0.11
~~~

### 6 digit, 2 fractional 0 decimal

Input:
~~~
1111.00
~~~

Result:
~~~
1111.0
~~~

### 6 digit, 2 fractional small decimal

Input:
~~~
1111.11
~~~

Result:
~~~
1111.11
~~~

### 6 digit, 2 fractional large decimal

Input:
~~~
9999.99
~~~

Result:
~~~
9999.99
~~~

### 7 digit 0, 3 fractional small decimal

Input:
~~~
0000.111
~~~

Result:
~~~
0.111
~~~

### 7 digit, 3 fractional 0 decimal

Input:
~~~
1111.000
~~~

Result:
~~~
1111.0
~~~

### 7 digit, 3 fractional small decimal

Input:
~~~
1111.111
~~~

Result:
~~~
1111.111
~~~

### 7 digit, 3 fractional large decimal

Input:
~~~
9999.999
~~~

Result:
~~~
9999.999
~~~

### 6 digit 0, 1 fractional small decimal

Input:
~~~
00000.1
~~~

Result:
~~~
0.1
~~~

### 6 digit, 1 fractional 0 decimal

Input:
~~~
11111.0
~~~

Result:
~~~
11111.0
~~~

### 6 digit, 1 fractional small decimal

Input:
~~~
11111.1
~~~

Result:
~~~
11111.1
~~~

### 6 digit, 1 fractional large decimal

Input:
~~~
99999.9
~~~

Result:
~~~
99999.9
~~~

### 7 digit 0, 2 fractional small decimal

Input:
~~~
00000.11
~~~

Result:
~~~
0.11
~~~

### 7 digit, 2 fractional 0 decimal

Input:
~~~
11111.00
~~~

Result:
~~~
11111.0
~~~

### 7 digit, 2 fractional small decimal

Input:
~~~
11111.11
~~~

Result:
~~~
11111.11
~~~

### 7 digit, 2 fractional large decimal

Input:
~~~
99999.99
~~~

Result:
~~~
99999.99
~~~

### 8 digit 0, 3 fractional small decimal

Input:
~~~
00000.111
~~~

Result:
~~~
0.111
~~~

### 8 digit, 3 fractional 0 decimal

Input:
~~~
11111.000
~~~

Result:
~~~
11111.0
~~~

### 8 digit, 3 fractional small decimal

Input:
~~~
11111.111
~~~

Result:
~~~
11111.111
~~~

### 8 digit, 3 fractional large decimal

Input:
~~~
99999.999
~~~

Result:
~~~
99999.999
~~~

### 7 digit 0, 1 fractional small decimal

Input:
~~~
000000.1
~~~

Result:
~~~
0.1
~~~

### 7 digit, 1 fractional 0 decimal

Input:
~~~
111111.0
~~~

Result:
~~~
111111.0
~~~

### 7 digit, 1 fractional small decimal

Input:
~~~
111111.1
~~~

Result:
~~~
111111.1
~~~

### 7 digit, 1 fractional large decimal

Input:
~~~
999999.9
~~~

Result:
~~~
999999.9
~~~

### 8 digit 0, 2 fractional small decimal

Input:
~~~
000000.11
~~~

Result:
~~~
0.11
~~~

### 8 digit, 2 fractional 0 decimal

Input:
~~~
111111.00
~~~

Result:
~~~
111111.0
~~~

### 8 digit, 2 fractional small decimal

Input:
~~~
111111.11
~~~

Result:
~~~
111111.11
~~~

### 8 digit, 2 fractional large decimal

Input:
~~~
999999.99
~~~

Result:
~~~
999999.99
~~~

### 9 digit 0, 3 fractional small decimal

Input:
~~~
000000.111
~~~

Result:
~~~
0.111
~~~

### 9 digit, 3 fractional 0 decimal

Input:
~~~
111111.000
~~~

Result:
~~~
111111.0
~~~

### 9 digit, 3 fractional small decimal

Input:
~~~
111111.111
~~~

Result:
~~~
111111.111
~~~

### 9 digit, 3 fractional large decimal

Input:
~~~
999999.999
~~~

Result:
~~~
999999.999
~~~

### 8 digit 0, 1 fractional small decimal

Input:
~~~
0000000.1
~~~

Result:
~~~
0.1
~~~

### 8 digit, 1 fractional 0 decimal

Input:
~~~
1111111.0
~~~

Result:
~~~
1111111.0
~~~

### 8 digit, 1 fractional small decimal

Input:
~~~
1111111.1
~~~

Result:
~~~
1111111.1
~~~

### 8 digit, 1 fractional large decimal

Input:
~~~
9999999.9
~~~

Result:
~~~
9999999.9
~~~

### 9 digit 0, 2 fractional small decimal

Input:
~~~
0000000.11
~~~

Result:
~~~
0.11
~~~

### 9 digit, 2 fractional 0 decimal

Input:
~~~
1111111.00
~~~

Result:
~~~
1111111.0
~~~

### 9 digit, 2 fractional small decimal

Input:
~~~
1111111.11
~~~

Result:
~~~
1111111.11
~~~

### 9 digit, 2 fractional large decimal

Input:
~~~
9999999.99
~~~

Result:
~~~
9999999.99
~~~

### 10 digit 0, 3 fractional small decimal

Input:
~~~
0000000.111
~~~

Result:
~~~
0.111
~~~

### 10 digit, 3 fractional 0 decimal

Input:
~~~
1111111.000
~~~

Result:
~~~
1111111.0
~~~

### 10 digit, 3 fractional small decimal

Input:
~~~
1111111.111
~~~

Result:
~~~
1111111.111
~~~

### 10 digit, 3 fractional large decimal

Input:
~~~
9999999.999
~~~

Result:
~~~
9999999.999
~~~

### 9 digit 0, 1 fractional small decimal

Input:
~~~
00000000.1
~~~

Result:
~~~
0.1
~~~

### 9 digit, 1 fractional 0 decimal

Input:
~~~
11111111.0
~~~

Result:
~~~
11111111.0
~~~

### 9 digit, 1 fractional small decimal

Input:
~~~
11111111.1
~~~

Result:
~~~
11111111.1
~~~

### 9 digit, 1 fractional large decimal

Input:
~~~
99999999.9
~~~

Result:
~~~
99999999.9
~~~

### 10 digit 0, 2 fractional small decimal

Input:
~~~
00000000.11
~~~

Result:
~~~
0.11
~~~

### 10 digit, 2 fractional 0 decimal

Input:
~~~
11111111.00
~~~

Result:
~~~
11111111.0
~~~

### 10 digit, 2 fractional small decimal

Input:
~~~
11111111.11
~~~

Result:
~~~
11111111.11
~~~

### 10 digit, 2 fractional large decimal

Input:
~~~
99999999.99
~~~

Result:
~~~
99999999.99
~~~

### 11 digit 0, 3 fractional small decimal

Input:
~~~
00000000.111
~~~

Result:
~~~
0.111
~~~

### 11 digit, 3 fractional 0 decimal

Input:
~~~
11111111.000
~~~

Result:
~~~
11111111.0
~~~

### 11 digit, 3 fractional small decimal

Input:
~~~
11111111.111
~~~

Result:
~~~
11111111.111
~~~

### 11 digit, 3 fractional large decimal

Input:
~~~
99999999.999
~~~

Result:
~~~
99999999.999
~~~

### 10 digit 0, 1 fractional small decimal

Input:
~~~
000000000.1
~~~

Result:
~~~
0.1
~~~

### 10 digit, 1 fractional 0 decimal

Input:
~~~
111111111.0
~~~

Result:
~~~
111111111.0
~~~

### 10 digit, 1 fractional small decimal

Input:
~~~
111111111.1
~~~

Result:
~~~
111111111.1
~~~

### 10 digit, 1 fractional large decimal

Input:
~~~
999999999.9
~~~

Result:
~~~
999999999.9
~~~

### 11 digit 0, 2 fractional small decimal

Input:
~~~
000000000.11
~~~

Result:
~~~
0.11
~~~

### 11 digit, 2 fractional 0 decimal

Input:
~~~
111111111.00
~~~

Result:
~~~
111111111.0
~~~

### 11 digit, 2 fractional small decimal

Input:
~~~
111111111.11
~~~

Result:
~~~
111111111.11
~~~

### 11 digit, 2 fractional large decimal

Input:
~~~
999999999.99
~~~

Result:
~~~
999999999.99
~~~

### 12 digit 0, 3 fractional small decimal

Input:
~~~
000000000.111
~~~

Result:
~~~
0.111
~~~

### 12 digit, 3 fractional 0 decimal

Input:
~~~
111111111.000
~~~

Result:
~~~
111111111.0
~~~

### 12 digit, 3 fractional small decimal

Input:
~~~
111111111.111
~~~

Result:
~~~
111111111.111
~~~

### 12 digit, 3 fractional large decimal

Input:
~~~
999999999.999
~~~

Result:
~~~
999999999.999
~~~

### 11 digit 0, 1 fractional small decimal

Input:
~~~
0000000000.1
~~~

Result:
~~~
0.1
~~~

### 11 digit, 1 fractional 0 decimal

Input:
~~~
1111111111.0
~~~

Result:
~~~
1111111111.0
~~~

### 11 digit, 1 fractional small decimal

Input:
~~~
1111111111.1
~~~

Result:
~~~
1111111111.1
~~~

### 11 digit, 1 fractional large decimal

Input:
~~~
9999999999.9
~~~

Result:
~~~
9999999999.9
~~~

### 12 digit 0, 2 fractional small decimal

Input:
~~~
0000000000.11
~~~

Result:
~~~
0.11
~~~

### 12 digit, 2 fractional 0 decimal

Input:
~~~
1111111111.00
~~~

Result:
~~~
1111111111.0
~~~

### 12 digit, 2 fractional small decimal

Input:
~~~
1111111111.11
~~~

Result:
~~~
1111111111.11
~~~

### 12 digit, 2 fractional large decimal

Input:
~~~
9999999999.99
~~~

Result:
~~~
9999999999.99
~~~

### 13 digit 0, 3 fractional small decimal

Input:
~~~
0000000000.111
~~~

Result:
~~~
0.111
~~~

### 13 digit, 3 fractional 0 decimal

Input:
~~~
1111111111.000
~~~

Result:
~~~
1111111111.0
~~~

### 13 digit, 3 fractional small decimal

Input:
~~~
1111111111.111
~~~

Result:
~~~
1111111111.111
~~~

### 13 digit, 3 fractional large decimal

Input:
~~~
9999999999.999
~~~

Result:
~~~
9999999999.999
~~~

### 12 digit 0, 1 fractional small decimal

Input:
~~~
00000000000.1
~~~

Result:
~~~
0.1
~~~

### 12 digit, 1 fractional 0 decimal

Input:
~~~
11111111111.0
~~~

Result:
~~~
11111111111.0
~~~

### 12 digit, 1 fractional small decimal

Input:
~~~
11111111111.1
~~~

Result:
~~~
11111111111.1
~~~

### 12 digit, 1 fractional large decimal

Input:
~~~
99999999999.9
~~~

Result:
~~~
99999999999.9
~~~

### 13 digit 0, 2 fractional small decimal

Input:
~~~
00000000000.11
~~~

Result:
~~~
0.11
~~~

### 13 digit, 2 fractional 0 decimal

Input:
~~~
11111111111.00
~~~

Result:
~~~
11111111111.0
~~~

### 13 digit, 2 fractional small decimal

Input:
~~~
11111111111.11
~~~

Result:
~~~
11111111111.11
~~~

### 13 digit, 2 fractional large decimal

Input:
~~~
99999999999.99
~~~

Result:
~~~
99999999999.99
~~~

### 14 digit 0, 3 fractional small decimal

Input:
~~~
00000000000.111
~~~

Result:
~~~
0.111
~~~

### 14 digit, 3 fractional 0 decimal

Input:
~~~
11111111111.000
~~~

Result:
~~~
11111111111.0
~~~

### 14 digit, 3 fractional small decimal

Input:
~~~
11111111111.111
~~~

Result:
~~~
11111111111.111
~~~

### 14 digit, 3 fractional large decimal

Input:
~~~
99999999999.999
~~~

Result:
~~~
99999999999.999
~~~

### 13 digit 0, 1 fractional small decimal

Input:
~~~
000000000000.1
~~~

Result:
~~~
0.1
~~~

### 13 digit, 1 fractional 0 decimal

Input:
~~~
111111111111.0
~~~

Result:
~~~
111111111111.0
~~~

### 13 digit, 1 fractional small decimal

Input:
~~~
111111111111.1
~~~

Result:
~~~
111111111111.1
~~~

### 13 digit, 1 fractional large decimal

Input:
~~~
999999999999.9
~~~

Result:
~~~
999999999999.9
~~~

### 14 digit 0, 2 fractional small decimal

Input:
~~~
000000000000.11
~~~

Result:
~~~
0.11
~~~

### 14 digit, 2 fractional 0 decimal

Input:
~~~
111111111111.00
~~~

Result:
~~~
111111111111.0
~~~

### 14 digit, 2 fractional small decimal

Input:
~~~
111111111111.11
~~~

Result:
~~~
111111111111.11
~~~

### 14 digit, 2 fractional large decimal

Input:
~~~
999999999999.99
~~~

Result:
~~~
999999999999.99
~~~

### 15 digit 0, 3 fractional small decimal

Input:
~~~
000000000000.111
~~~

Result:
~~~
0.111
~~~

### 15 digit, 3 fractional 0 decimal

Input:
~~~
111111111111.000
~~~

Result:
~~~
111111111111.0
~~~

### 15 digit, 3 fractional small decimal

Input:
~~~
111111111111.111
~~~

Result:
~~~
111111111111.111
~~~

### 15 digit, 3 fractional large decimal

Input:
~~~
999999999999.999
~~~

Result:
~~~
999999999999.999
~~~

### too many digit 0 decimal

Input:
~~~
000000000000000.0
~~~

Expects Parse Error
~~~
>>000000000000000.0<<
  ---------------^ (0x2e) Illegal position for decimal point in Decimal after '000000000000000'
~~~


### too many fractional digits 0 decimal

Input:
~~~
000000000000.0000
~~~

Expects Parse Error
~~~
>>000000000000.0000<<
  ----------------^ (0x30) Decimal too long: 17 characters
~~~


### too many digit 9 decimal

Input:
~~~
999999999999999.9
~~~

Expects Parse Error
~~~
>>999999999999999.9<<
  ---------------^ (0x2e) Illegal position for decimal point in Decimal after '999999999999999'
~~~


### too many fractional digits 9 decimal

Input:
~~~
999999999999.9999
~~~

Expects Parse Error
~~~
>>999999999999.9999<<
  ----------------^ (0x39) Decimal too long: 17 characters
~~~



## param-dict


### basic parameterised dict

Input:
~~~
abc=123;a=1;b=2, def=456, ghi=789;q=9;r="+w"
~~~

Result:
~~~
abc=123;a=1;b=2, def=456, ghi=789;q=9;r="+w"
~~~

### single item parameterised dict

Input:
~~~
a=b; q=1.0
~~~

Result:
~~~
a=b;q=1.0
~~~

### list item parameterised dictionary

Input:
~~~
a=(1 2); q=1.0
~~~

Result:
~~~
a=(1 2);q=1.0
~~~

### missing parameter value parameterised dict

Input:
~~~
a=3;c;d=5
~~~

Result:
~~~
a=3;c;d=5
~~~

### terminal missing parameter value parameterised dict

Input:
~~~
a=3;c=5;d
~~~

Result:
~~~
a=3;c=5;d
~~~

### no whitespace parameterised dict

Input:
~~~
a=b;c=1,d=e;f=2
~~~

Result:
~~~
a=b;c=1, d=e;f=2
~~~

### whitespace before = parameterised dict

Input:
~~~
a=b;q =0.5
~~~

Expects Parse Error
~~~
>>a=b;q =0.5<<
  ------^ (0x3d) Expected COMMA in Dictionary, found: '=' (\u003d)
~~~


### whitespace after = parameterised dict

Input:
~~~
a=b;q= 0.5
~~~

Expects Parse Error
~~~
>>a=b;q= 0.5<<
  ------^ (0x20) Unexpected start character in Bare Item: ' ' (\u0020)
~~~


### whitespace before ; parameterised dict

Input:
~~~
a=b ;q=0.5
~~~

Expects Parse Error
~~~
>>a=b ;q=0.5<<
  ----^ (0x3b) Expected COMMA in Dictionary, found: ';' (\u003b)
~~~


### whitespace after ; parameterised dict

Input:
~~~
a=b; q=0.5
~~~

Result:
~~~
a=b;q=0.5
~~~

### extra whitespace parameterised dict

Input:
~~~
a=b;  c=1  ,  d=e; f=2; g=3
~~~

Result:
~~~
a=b;c=1, d=e;f=2;g=3
~~~

### two lines parameterised list

Input:
~~~
a=b;c=1
d=e;f=2
~~~

Result:
~~~
a=b;c=1, d=e;f=2
~~~

### trailing comma parameterised list

Input:
~~~
a=b; q=1.0,
~~~

Expects Parse Error
~~~
>>a=b; q=1.0,<<
  -----------^ Found trailing COMMA in Dictionary
~~~


### empty item parameterised list

Input:
~~~
a=b; q=1.0,,c=d
~~~

Expects Parse Error
~~~
>>a=b; q=1.0,,c=d<<
  -----------^ (0x2c) Key must start with LCALPHA or '*': ',' (\u002c)
~~~



## param-list


### basic parameterised list

Input:
~~~
abc_123;a=1;b=2; cdef_456, ghi;q=9;r="+w"
~~~

Result:
~~~
abc_123;a=1;b=2;cdef_456, ghi;q=9;r="+w"
~~~

### single item parameterised list

Input:
~~~
text/html;q=1.0
~~~

Result:
~~~
text/html;q=1.0
~~~

### missing parameter value parameterised list

Input:
~~~
text/html;a;q=1.0
~~~

Result:
~~~
text/html;a;q=1.0
~~~

### missing terminal parameter value parameterised list

Input:
~~~
text/html;q=1.0;a
~~~

Result:
~~~
text/html;q=1.0;a
~~~

### no whitespace parameterised list

Input:
~~~
text/html,text/plain;q=0.5
~~~

Result:
~~~
text/html, text/plain;q=0.5
~~~

### whitespace before = parameterised list

Input:
~~~
text/html, text/plain;q =0.5
~~~

Expects Parse Error
~~~
>>text/html, text/plain;q =0.5<<
  ------------------------^ (0x3d) Expected COMMA in List, got: '=' (\u003d)
~~~


### whitespace after = parameterised list

Input:
~~~
text/html, text/plain;q= 0.5
~~~

Expects Parse Error
~~~
>>text/html, text/plain;q= 0.5<<
  ------------------------^ (0x20) Unexpected start character in Bare Item: ' ' (\u0020)
~~~


### whitespace before ; parameterised list

Input:
~~~
text/html, text/plain ;q=0.5
~~~

Expects Parse Error
~~~
>>text/html, text/plain ;q=0.5<<
  ----------------------^ (0x3b) Expected COMMA in List, got: ';' (\u003b)
~~~


### whitespace after ; parameterised list

Input:
~~~
text/html, text/plain; q=0.5
~~~

Result:
~~~
text/html, text/plain;q=0.5
~~~

### extra whitespace parameterised list

Input:
~~~
text/html  ,  text/plain;  q=0.5;  charset=utf-8
~~~

Result:
~~~
text/html, text/plain;q=0.5;charset=utf-8
~~~

### two lines parameterised list

Input:
~~~
text/html
text/plain;q=0.5
~~~

Result:
~~~
text/html, text/plain;q=0.5
~~~

### trailing comma parameterised list

Input:
~~~
text/html,text/plain;q=0.5,
~~~

Expects Parse Error
~~~
>>text/html,text/plain;q=0.5,<<
  ---------------------------^ Found trailing COMMA in List
~~~


### empty item parameterised list

Input:
~~~
text/html,,text/plain;q=0.5,
~~~

Expects Parse Error
~~~
>>text/html,,text/plain;q=0.5,<<
  ----------^ (0x2c) Unexpected start character in Bare Item: ',' (\u002c)
~~~



## param-listlist


### parameterised inner list

Input:
~~~
(abc_123);a=1;b=2, cdef_456
~~~

Result:
~~~
(abc_123);a=1;b=2, cdef_456
~~~

### parameterised inner list item

Input:
~~~
(abc_123;a=1;b=2;cdef_456)
~~~

Result:
~~~
(abc_123;a=1;b=2;cdef_456)
~~~

### parameterised inner list with parameterised item

Input:
~~~
(abc_123;a=1;b=2);cdef_456
~~~

Result:
~~~
(abc_123;a=1;b=2);cdef_456
~~~

