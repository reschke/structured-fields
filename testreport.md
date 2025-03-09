# Test Report


## token-generated


### 0x00 in token

Input:
~~~
a a
~~~

Expects Parse Error
~~~
>>a a<<
  -^ (0x00) Extra characters in string parsed as Item
~~~


### 0x01 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x01) Extra characters in string parsed as Item
~~~


### 0x02 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x02) Extra characters in string parsed as Item
~~~


### 0x03 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x03) Extra characters in string parsed as Item
~~~


### 0x04 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x04) Extra characters in string parsed as Item
~~~


### 0x05 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x05) Extra characters in string parsed as Item
~~~


### 0x06 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x06) Extra characters in string parsed as Item
~~~


### 0x07 in token

Input:
~~~
a a
~~~

Expects Parse Error
~~~
>>a a<<
  -^ (0x07) Extra characters in string parsed as Item
~~~


### 0x08 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x08) Extra characters in string parsed as Item
~~~


### 0x09 in token

Input:
~~~
a	a
~~~

Expects Parse Error
~~~
>>a	a<<
  -^ (0x09) Extra characters in string parsed as Item
~~~


### 0x0a in token

Input:
~~~
a
a
~~~

Expects Parse Error
~~~
>>a
a<<
  -^ (0x0a) Extra characters in string parsed as Item
~~~


### 0x0b in token

Input:
~~~
a
a
~~~

Expects Parse Error
~~~
>>a
a<<
  -^ (0x0b) Extra characters in string parsed as Item
~~~


### 0x0c in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x0c) Extra characters in string parsed as Item
~~~


### 0x0d in token

Input:
~~~
a
a
~~~

Expects Parse Error
~~~
>>a
a<<
  -^ (0x0d) Extra characters in string parsed as Item
~~~


### 0x0e in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x0e) Extra characters in string parsed as Item
~~~


### 0x0f in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x0f) Extra characters in string parsed as Item
~~~


### 0x10 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x10) Extra characters in string parsed as Item
~~~


### 0x11 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x11) Extra characters in string parsed as Item
~~~


### 0x12 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x12) Extra characters in string parsed as Item
~~~


### 0x13 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x13) Extra characters in string parsed as Item
~~~


### 0x14 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x14) Extra characters in string parsed as Item
~~~


### 0x15 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x15) Extra characters in string parsed as Item
~~~


### 0x16 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x16) Extra characters in string parsed as Item
~~~


### 0x17 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x17) Extra characters in string parsed as Item
~~~


### 0x18 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x18) Extra characters in string parsed as Item
~~~


### 0x19 in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x19) Extra characters in string parsed as Item
~~~


### 0x1a in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x1a) Extra characters in string parsed as Item
~~~


### 0x1b in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x1b) Extra characters in string parsed as Item
~~~


### 0x1c in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x1c) Extra characters in string parsed as Item
~~~


### 0x1d in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x1d) Extra characters in string parsed as Item
~~~


### 0x1e in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x1e) Extra characters in string parsed as Item
~~~


### 0x1f in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x1f) Extra characters in string parsed as Item
~~~


### 0x20 in token

Input:
~~~
a a
~~~

Expects Parse Error
~~~
>>a a<<
  --^ (0x61) Extra characters in string parsed as Item
~~~


### 0x21 in token

Input:
~~~
a!a
~~~

Result:
~~~
a!a
~~~

### 0x22 in token

Input:
~~~
a"a
~~~

Expects Parse Error
~~~
>>a"a<<
  -^ (0x22) Extra characters in string parsed as Item
~~~


### 0x23 in token

Input:
~~~
a#a
~~~

Result:
~~~
a#a
~~~

### 0x24 in token

Input:
~~~
a$a
~~~

Result:
~~~
a$a
~~~

### 0x25 in token

Input:
~~~
a%a
~~~

Result:
~~~
a%a
~~~

### 0x26 in token

Input:
~~~
a&a
~~~

Result:
~~~
a&a
~~~

### 0x27 in token

Input:
~~~
a'a
~~~

Result:
~~~
a'a
~~~

### 0x28 in token

Input:
~~~
a(a
~~~

Expects Parse Error
~~~
>>a(a<<
  -^ (0x28) Extra characters in string parsed as Item
~~~


### 0x29 in token

Input:
~~~
a)a
~~~

Expects Parse Error
~~~
>>a)a<<
  -^ (0x29) Extra characters in string parsed as Item
~~~


### 0x2a in token

Input:
~~~
a*a
~~~

Result:
~~~
a*a
~~~

### 0x2b in token

Input:
~~~
a+a
~~~

Result:
~~~
a+a
~~~

### 0x2c in token

Input:
~~~
a,a
~~~

Expects Parse Error
~~~
>>a,a<<
  -^ (0x2c) Extra characters in string parsed as Item
~~~


### 0x2d in token

Input:
~~~
a-a
~~~

Result:
~~~
a-a
~~~

### 0x2e in token

Input:
~~~
a.a
~~~

Result:
~~~
a.a
~~~

### 0x2f in token

Input:
~~~
a/a
~~~

Result:
~~~
a/a
~~~

### 0x30 in token

Input:
~~~
a0a
~~~

Result:
~~~
a0a
~~~

### 0x31 in token

Input:
~~~
a1a
~~~

Result:
~~~
a1a
~~~

### 0x32 in token

Input:
~~~
a2a
~~~

Result:
~~~
a2a
~~~

### 0x33 in token

Input:
~~~
a3a
~~~

Result:
~~~
a3a
~~~

### 0x34 in token

Input:
~~~
a4a
~~~

Result:
~~~
a4a
~~~

### 0x35 in token

Input:
~~~
a5a
~~~

Result:
~~~
a5a
~~~

### 0x36 in token

Input:
~~~
a6a
~~~

Result:
~~~
a6a
~~~

### 0x37 in token

Input:
~~~
a7a
~~~

Result:
~~~
a7a
~~~

### 0x38 in token

Input:
~~~
a8a
~~~

Result:
~~~
a8a
~~~

### 0x39 in token

Input:
~~~
a9a
~~~

Result:
~~~
a9a
~~~

### 0x3a in token

Input:
~~~
a:a
~~~

Result:
~~~
a:a
~~~

### 0x3b in token

Input:
~~~
a;a
~~~

Result:
~~~
a;a
~~~

### 0x3c in token

Input:
~~~
a<a
~~~

Expects Parse Error
~~~
>>a<a<<
  -^ (0x3c) Extra characters in string parsed as Item
~~~


### 0x3d in token

Input:
~~~
a=a
~~~

Expects Parse Error
~~~
>>a=a<<
  -^ (0x3d) Extra characters in string parsed as Item
~~~


### 0x3e in token

Input:
~~~
a>a
~~~

Expects Parse Error
~~~
>>a>a<<
  -^ (0x3e) Extra characters in string parsed as Item
~~~


### 0x3f in token

Input:
~~~
a?a
~~~

Expects Parse Error
~~~
>>a?a<<
  -^ (0x3f) Extra characters in string parsed as Item
~~~


### 0x40 in token

Input:
~~~
a@a
~~~

Expects Parse Error
~~~
>>a@a<<
  -^ (0x40) Extra characters in string parsed as Item
~~~


### 0x41 in token

Input:
~~~
aAa
~~~

Result:
~~~
aAa
~~~

### 0x42 in token

Input:
~~~
aBa
~~~

Result:
~~~
aBa
~~~

### 0x43 in token

Input:
~~~
aCa
~~~

Result:
~~~
aCa
~~~

### 0x44 in token

Input:
~~~
aDa
~~~

Result:
~~~
aDa
~~~

### 0x45 in token

Input:
~~~
aEa
~~~

Result:
~~~
aEa
~~~

### 0x46 in token

Input:
~~~
aFa
~~~

Result:
~~~
aFa
~~~

### 0x47 in token

Input:
~~~
aGa
~~~

Result:
~~~
aGa
~~~

### 0x48 in token

Input:
~~~
aHa
~~~

Result:
~~~
aHa
~~~

### 0x49 in token

Input:
~~~
aIa
~~~

Result:
~~~
aIa
~~~

### 0x4a in token

Input:
~~~
aJa
~~~

Result:
~~~
aJa
~~~

### 0x4b in token

Input:
~~~
aKa
~~~

Result:
~~~
aKa
~~~

### 0x4c in token

Input:
~~~
aLa
~~~

Result:
~~~
aLa
~~~

### 0x4d in token

Input:
~~~
aMa
~~~

Result:
~~~
aMa
~~~

### 0x4e in token

Input:
~~~
aNa
~~~

Result:
~~~
aNa
~~~

### 0x4f in token

Input:
~~~
aOa
~~~

Result:
~~~
aOa
~~~

### 0x50 in token

Input:
~~~
aPa
~~~

Result:
~~~
aPa
~~~

### 0x51 in token

Input:
~~~
aQa
~~~

Result:
~~~
aQa
~~~

### 0x52 in token

Input:
~~~
aRa
~~~

Result:
~~~
aRa
~~~

### 0x53 in token

Input:
~~~
aSa
~~~

Result:
~~~
aSa
~~~

### 0x54 in token

Input:
~~~
aTa
~~~

Result:
~~~
aTa
~~~

### 0x55 in token

Input:
~~~
aUa
~~~

Result:
~~~
aUa
~~~

### 0x56 in token

Input:
~~~
aVa
~~~

Result:
~~~
aVa
~~~

### 0x57 in token

Input:
~~~
aWa
~~~

Result:
~~~
aWa
~~~

### 0x58 in token

Input:
~~~
aXa
~~~

Result:
~~~
aXa
~~~

### 0x59 in token

Input:
~~~
aYa
~~~

Result:
~~~
aYa
~~~

### 0x5a in token

Input:
~~~
aZa
~~~

Result:
~~~
aZa
~~~

### 0x5b in token

Input:
~~~
a[a
~~~

Expects Parse Error
~~~
>>a[a<<
  -^ (0x5b) Extra characters in string parsed as Item
~~~


### 0x5c in token

Input:
~~~
a\a
~~~

Expects Parse Error
~~~
>>a\a<<
  -^ (0x5c) Extra characters in string parsed as Item
~~~


### 0x5d in token

Input:
~~~
a]a
~~~

Expects Parse Error
~~~
>>a]a<<
  -^ (0x5d) Extra characters in string parsed as Item
~~~


### 0x5e in token

Input:
~~~
a^a
~~~

Result:
~~~
a^a
~~~

### 0x5f in token

Input:
~~~
a_a
~~~

Result:
~~~
a_a
~~~

### 0x60 in token

Input:
~~~
a`a
~~~

Result:
~~~
a`a
~~~

### 0x61 in token

Input:
~~~
aaa
~~~

Result:
~~~
aaa
~~~

### 0x62 in token

Input:
~~~
aba
~~~

Result:
~~~
aba
~~~

### 0x63 in token

Input:
~~~
aca
~~~

Result:
~~~
aca
~~~

### 0x64 in token

Input:
~~~
ada
~~~

Result:
~~~
ada
~~~

### 0x65 in token

Input:
~~~
aea
~~~

Result:
~~~
aea
~~~

### 0x66 in token

Input:
~~~
afa
~~~

Result:
~~~
afa
~~~

### 0x67 in token

Input:
~~~
aga
~~~

Result:
~~~
aga
~~~

### 0x68 in token

Input:
~~~
aha
~~~

Result:
~~~
aha
~~~

### 0x69 in token

Input:
~~~
aia
~~~

Result:
~~~
aia
~~~

### 0x6a in token

Input:
~~~
aja
~~~

Result:
~~~
aja
~~~

### 0x6b in token

Input:
~~~
aka
~~~

Result:
~~~
aka
~~~

### 0x6c in token

Input:
~~~
ala
~~~

Result:
~~~
ala
~~~

### 0x6d in token

Input:
~~~
ama
~~~

Result:
~~~
ama
~~~

### 0x6e in token

Input:
~~~
ana
~~~

Result:
~~~
ana
~~~

### 0x6f in token

Input:
~~~
aoa
~~~

Result:
~~~
aoa
~~~

### 0x70 in token

Input:
~~~
apa
~~~

Result:
~~~
apa
~~~

### 0x71 in token

Input:
~~~
aqa
~~~

Result:
~~~
aqa
~~~

### 0x72 in token

Input:
~~~
ara
~~~

Result:
~~~
ara
~~~

### 0x73 in token

Input:
~~~
asa
~~~

Result:
~~~
asa
~~~

### 0x74 in token

Input:
~~~
ata
~~~

Result:
~~~
ata
~~~

### 0x75 in token

Input:
~~~
aua
~~~

Result:
~~~
aua
~~~

### 0x76 in token

Input:
~~~
ava
~~~

Result:
~~~
ava
~~~

### 0x77 in token

Input:
~~~
awa
~~~

Result:
~~~
awa
~~~

### 0x78 in token

Input:
~~~
axa
~~~

Result:
~~~
axa
~~~

### 0x79 in token

Input:
~~~
aya
~~~

Result:
~~~
aya
~~~

### 0x7a in token

Input:
~~~
aza
~~~

Result:
~~~
aza
~~~

### 0x7b in token

Input:
~~~
a{a
~~~

Expects Parse Error
~~~
>>a{a<<
  -^ (0x7b) Extra characters in string parsed as Item
~~~


### 0x7c in token

Input:
~~~
a|a
~~~

Result:
~~~
a|a
~~~

### 0x7d in token

Input:
~~~
a}a
~~~

Expects Parse Error
~~~
>>a}a<<
  -^ (0x7d) Extra characters in string parsed as Item
~~~


### 0x7e in token

Input:
~~~
a~a
~~~

Result:
~~~
a~a
~~~

### 0x7f in token

Input:
~~~
aa
~~~

Expects Parse Error
~~~
>>aa<<
  -^ (0x7f) Extra characters in string parsed as Item
~~~


### 0x00 starting a token

Input:
~~~
 a
~~~

Expects Parse Error
~~~
>> a<<
  ^ (0x00) Unexpected start character in Bare Item: ' ' (\u0000)
~~~


### 0x01 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x01) Unexpected start character in Bare Item: '' (\u0001)
~~~


### 0x02 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x02) Unexpected start character in Bare Item: '' (\u0002)
~~~


### 0x03 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x03) Unexpected start character in Bare Item: '' (\u0003)
~~~


### 0x04 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x04) Unexpected start character in Bare Item: '' (\u0004)
~~~


### 0x05 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x05) Unexpected start character in Bare Item: '' (\u0005)
~~~


### 0x06 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x06) Unexpected start character in Bare Item: '' (\u0006)
~~~


### 0x07 starting a token

Input:
~~~
 a
~~~

Expects Parse Error
~~~
>> a<<
  ^ (0x07) Unexpected start character in Bare Item: ' ' (\u0007)
~~~


### 0x08 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x08) Unexpected start character in Bare Item: '' (\u0008)
~~~


### 0x09 starting a token

Input:
~~~
	a
~~~

Expects Parse Error
~~~
>>	a<<
  ^ (0x09) Unexpected start character in Bare Item: HTAB (\u0009)
~~~


### 0x0a starting a token

Input:
~~~

a
~~~

Expects Parse Error
~~~
>>
a<<
  ^ (0x0a) Unexpected start character in Bare Item: '
' (\u000a)
~~~


### 0x0b starting a token

Input:
~~~

a
~~~

Expects Parse Error
~~~
>>
a<<
  ^ (0x0b) Unexpected start character in Bare Item: '
' (\u000b)
~~~


### 0x0c starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x0c) Unexpected start character in Bare Item: '' (\u000c)
~~~


### 0x0d starting a token

Input:
~~~

a
~~~

Expects Parse Error
~~~
>>
a<<
  ^ (0x0d) Unexpected start character in Bare Item: '
' (\u000d)
~~~


### 0x0e starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x0e) Unexpected start character in Bare Item: '' (\u000e)
~~~


### 0x0f starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x0f) Unexpected start character in Bare Item: '' (\u000f)
~~~


### 0x10 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x10) Unexpected start character in Bare Item: '' (\u0010)
~~~


### 0x11 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x11) Unexpected start character in Bare Item: '' (\u0011)
~~~


### 0x12 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x12) Unexpected start character in Bare Item: '' (\u0012)
~~~


### 0x13 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x13) Unexpected start character in Bare Item: '' (\u0013)
~~~


### 0x14 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x14) Unexpected start character in Bare Item: '' (\u0014)
~~~


### 0x15 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x15) Unexpected start character in Bare Item: '' (\u0015)
~~~


### 0x16 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x16) Unexpected start character in Bare Item: '' (\u0016)
~~~


### 0x17 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x17) Unexpected start character in Bare Item: '' (\u0017)
~~~


### 0x18 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x18) Unexpected start character in Bare Item: '' (\u0018)
~~~


### 0x19 starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x19) Unexpected start character in Bare Item: '' (\u0019)
~~~


### 0x1a starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x1a) Unexpected start character in Bare Item: '' (\u001a)
~~~


### 0x1b starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x1b) Unexpected start character in Bare Item: '' (\u001b)
~~~


### 0x1c starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x1c) Unexpected start character in Bare Item: '' (\u001c)
~~~


### 0x1d starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x1d) Unexpected start character in Bare Item: '' (\u001d)
~~~


### 0x1e starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x1e) Unexpected start character in Bare Item: '' (\u001e)
~~~


### 0x1f starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x1f) Unexpected start character in Bare Item: '' (\u001f)
~~~


### 0x20 starting a token

Input:
~~~
 a
~~~

Result:
~~~
a
~~~

### 0x21 starting a token

Input:
~~~
!a
~~~

Expects Parse Error
~~~
>>!a<<
  ^ (0x21) Unexpected start character in Bare Item: '!' (\u0021)
~~~


### 0x22 starting a token

Input:
~~~
"a
~~~

Expects Parse Error
~~~
>>"a<<
  --^ Closing DQUOTE missing
~~~


### 0x23 starting a token

Input:
~~~
#a
~~~

Expects Parse Error
~~~
>>#a<<
  ^ (0x23) Unexpected start character in Bare Item: '#' (\u0023)
~~~


### 0x24 starting a token

Input:
~~~
$a
~~~

Expects Parse Error
~~~
>>$a<<
  ^ (0x24) Unexpected start character in Bare Item: '$' (\u0024)
~~~


### 0x25 starting a token

Input:
~~~
%a
~~~

Expects Parse Error
~~~
>>%a<<
  --^ DisplayString must continue with a double quote: ''
~~~


### 0x26 starting a token

Input:
~~~
&a
~~~

Expects Parse Error
~~~
>>&a<<
  ^ (0x26) Unexpected start character in Bare Item: '&' (\u0026)
~~~


### 0x27 starting a token

Input:
~~~
'a
~~~

Expects Parse Error
~~~
>>'a<<
  ^ (0x27) Unexpected start character in Bare Item: ''' (\u0027)
~~~


### 0x28 starting a token

Input:
~~~
(a
~~~

Expects Parse Error
~~~
>>(a<<
  ^ (0x28) Unexpected start character in Bare Item: '(' (\u0028)
~~~


### 0x29 starting a token

Input:
~~~
)a
~~~

Expects Parse Error
~~~
>>)a<<
  ^ (0x29) Unexpected start character in Bare Item: ')' (\u0029)
~~~


### 0x2a starting a token

Input:
~~~
*a
~~~

Result:
~~~
*a
~~~

### 0x2b starting a token

Input:
~~~
+a
~~~

Expects Parse Error
~~~
>>+a<<
  ^ (0x2b) Unexpected start character in Bare Item: '+' (\u002b)
~~~


### 0x2c starting a token

Input:
~~~
,a
~~~

Expects Parse Error
~~~
>>,a<<
  ^ (0x2c) Unexpected start character in Bare Item: ',' (\u002c)
~~~


### 0x2d starting a token

Input:
~~~
-a
~~~

Expects Parse Error
~~~
>>-a<<
  -^ (0x61) Illegal start for Integer or Decimal: 'a'
~~~


### 0x2e starting a token

Input:
~~~
.a
~~~

Expects Parse Error
~~~
>>.a<<
  ^ (0x2e) Unexpected start character in Bare Item: '.' (\u002e)
~~~


### 0x2f starting a token

Input:
~~~
/a
~~~

Expects Parse Error
~~~
>>/a<<
  ^ (0x2f) Unexpected start character in Bare Item: '/' (\u002f)
~~~


### 0x30 starting a token

Input:
~~~
0a
~~~

Expects Parse Error
~~~
>>0a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x31 starting a token

Input:
~~~
1a
~~~

Expects Parse Error
~~~
>>1a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x32 starting a token

Input:
~~~
2a
~~~

Expects Parse Error
~~~
>>2a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x33 starting a token

Input:
~~~
3a
~~~

Expects Parse Error
~~~
>>3a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x34 starting a token

Input:
~~~
4a
~~~

Expects Parse Error
~~~
>>4a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x35 starting a token

Input:
~~~
5a
~~~

Expects Parse Error
~~~
>>5a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x36 starting a token

Input:
~~~
6a
~~~

Expects Parse Error
~~~
>>6a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x37 starting a token

Input:
~~~
7a
~~~

Expects Parse Error
~~~
>>7a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x38 starting a token

Input:
~~~
8a
~~~

Expects Parse Error
~~~
>>8a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x39 starting a token

Input:
~~~
9a
~~~

Expects Parse Error
~~~
>>9a<<
  -^ (0x61) Extra characters in string parsed as Item
~~~


### 0x3a starting a token

Input:
~~~
:a
~~~

Expects Parse Error
~~~
>>:a<<
  --^ Byte Sequence must end with COLON: 'a'
~~~


### 0x3b starting a token

Input:
~~~
;a
~~~

Expects Parse Error
~~~
>>;a<<
  ^ (0x3b) Unexpected start character in Bare Item: ';' (\u003b)
~~~


### 0x3c starting a token

Input:
~~~
<a
~~~

Expects Parse Error
~~~
>><a<<
  ^ (0x3c) Unexpected start character in Bare Item: '<' (\u003c)
~~~


### 0x3d starting a token

Input:
~~~
=a
~~~

Expects Parse Error
~~~
>>=a<<
  ^ (0x3d) Unexpected start character in Bare Item: '=' (\u003d)
~~~


### 0x3e starting a token

Input:
~~~
>a
~~~

Expects Parse Error
~~~
>>>a<<
  ^ (0x3e) Unexpected start character in Bare Item: '>' (\u003e)
~~~


### 0x3f starting a token

Input:
~~~
?a
~~~

Expects Parse Error
~~~
>>?a<<
  -^ (0x61) Expected '0' or '1' in Boolean, found 'a'
~~~


### 0x40 starting a token

Input:
~~~
@a
~~~

Expects Parse Error
~~~
>>@a<<
  -^ (0x61) Illegal start inside a Date: 'a'
~~~


### 0x41 starting a token

Input:
~~~
Aa
~~~

Result:
~~~
Aa
~~~

### 0x42 starting a token

Input:
~~~
Ba
~~~

Result:
~~~
Ba
~~~

### 0x43 starting a token

Input:
~~~
Ca
~~~

Result:
~~~
Ca
~~~

### 0x44 starting a token

Input:
~~~
Da
~~~

Result:
~~~
Da
~~~

### 0x45 starting a token

Input:
~~~
Ea
~~~

Result:
~~~
Ea
~~~

### 0x46 starting a token

Input:
~~~
Fa
~~~

Result:
~~~
Fa
~~~

### 0x47 starting a token

Input:
~~~
Ga
~~~

Result:
~~~
Ga
~~~

### 0x48 starting a token

Input:
~~~
Ha
~~~

Result:
~~~
Ha
~~~

### 0x49 starting a token

Input:
~~~
Ia
~~~

Result:
~~~
Ia
~~~

### 0x4a starting a token

Input:
~~~
Ja
~~~

Result:
~~~
Ja
~~~

### 0x4b starting a token

Input:
~~~
Ka
~~~

Result:
~~~
Ka
~~~

### 0x4c starting a token

Input:
~~~
La
~~~

Result:
~~~
La
~~~

### 0x4d starting a token

Input:
~~~
Ma
~~~

Result:
~~~
Ma
~~~

### 0x4e starting a token

Input:
~~~
Na
~~~

Result:
~~~
Na
~~~

### 0x4f starting a token

Input:
~~~
Oa
~~~

Result:
~~~
Oa
~~~

### 0x50 starting a token

Input:
~~~
Pa
~~~

Result:
~~~
Pa
~~~

### 0x51 starting a token

Input:
~~~
Qa
~~~

Result:
~~~
Qa
~~~

### 0x52 starting a token

Input:
~~~
Ra
~~~

Result:
~~~
Ra
~~~

### 0x53 starting a token

Input:
~~~
Sa
~~~

Result:
~~~
Sa
~~~

### 0x54 starting a token

Input:
~~~
Ta
~~~

Result:
~~~
Ta
~~~

### 0x55 starting a token

Input:
~~~
Ua
~~~

Result:
~~~
Ua
~~~

### 0x56 starting a token

Input:
~~~
Va
~~~

Result:
~~~
Va
~~~

### 0x57 starting a token

Input:
~~~
Wa
~~~

Result:
~~~
Wa
~~~

### 0x58 starting a token

Input:
~~~
Xa
~~~

Result:
~~~
Xa
~~~

### 0x59 starting a token

Input:
~~~
Ya
~~~

Result:
~~~
Ya
~~~

### 0x5a starting a token

Input:
~~~
Za
~~~

Result:
~~~
Za
~~~

### 0x5b starting a token

Input:
~~~
[a
~~~

Expects Parse Error
~~~
>>[a<<
  ^ (0x5b) Unexpected start character in Bare Item: '[' (\u005b)
~~~


### 0x5c starting a token

Input:
~~~
\a
~~~

Expects Parse Error
~~~
>>\a<<
  ^ (0x5c) Unexpected start character in Bare Item: '\' (\u005c)
~~~


### 0x5d starting a token

Input:
~~~
]a
~~~

Expects Parse Error
~~~
>>]a<<
  ^ (0x5d) Unexpected start character in Bare Item: ']' (\u005d)
~~~


### 0x5e starting a token

Input:
~~~
^a
~~~

Expects Parse Error
~~~
>>^a<<
  ^ (0x5e) Unexpected start character in Bare Item: '^' (\u005e)
~~~


### 0x5f starting a token

Input:
~~~
_a
~~~

Expects Parse Error
~~~
>>_a<<
  ^ (0x5f) Unexpected start character in Bare Item: '_' (\u005f)
~~~


### 0x60 starting a token

Input:
~~~
`a
~~~

Expects Parse Error
~~~
>>`a<<
  ^ (0x60) Unexpected start character in Bare Item: '`' (\u0060)
~~~


### 0x61 starting a token

Input:
~~~
aa
~~~

Result:
~~~
aa
~~~

### 0x62 starting a token

Input:
~~~
ba
~~~

Result:
~~~
ba
~~~

### 0x63 starting a token

Input:
~~~
ca
~~~

Result:
~~~
ca
~~~

### 0x64 starting a token

Input:
~~~
da
~~~

Result:
~~~
da
~~~

### 0x65 starting a token

Input:
~~~
ea
~~~

Result:
~~~
ea
~~~

### 0x66 starting a token

Input:
~~~
fa
~~~

Result:
~~~
fa
~~~

### 0x67 starting a token

Input:
~~~
ga
~~~

Result:
~~~
ga
~~~

### 0x68 starting a token

Input:
~~~
ha
~~~

Result:
~~~
ha
~~~

### 0x69 starting a token

Input:
~~~
ia
~~~

Result:
~~~
ia
~~~

### 0x6a starting a token

Input:
~~~
ja
~~~

Result:
~~~
ja
~~~

### 0x6b starting a token

Input:
~~~
ka
~~~

Result:
~~~
ka
~~~

### 0x6c starting a token

Input:
~~~
la
~~~

Result:
~~~
la
~~~

### 0x6d starting a token

Input:
~~~
ma
~~~

Result:
~~~
ma
~~~

### 0x6e starting a token

Input:
~~~
na
~~~

Result:
~~~
na
~~~

### 0x6f starting a token

Input:
~~~
oa
~~~

Result:
~~~
oa
~~~

### 0x70 starting a token

Input:
~~~
pa
~~~

Result:
~~~
pa
~~~

### 0x71 starting a token

Input:
~~~
qa
~~~

Result:
~~~
qa
~~~

### 0x72 starting a token

Input:
~~~
ra
~~~

Result:
~~~
ra
~~~

### 0x73 starting a token

Input:
~~~
sa
~~~

Result:
~~~
sa
~~~

### 0x74 starting a token

Input:
~~~
ta
~~~

Result:
~~~
ta
~~~

### 0x75 starting a token

Input:
~~~
ua
~~~

Result:
~~~
ua
~~~

### 0x76 starting a token

Input:
~~~
va
~~~

Result:
~~~
va
~~~

### 0x77 starting a token

Input:
~~~
wa
~~~

Result:
~~~
wa
~~~

### 0x78 starting a token

Input:
~~~
xa
~~~

Result:
~~~
xa
~~~

### 0x79 starting a token

Input:
~~~
ya
~~~

Result:
~~~
ya
~~~

### 0x7a starting a token

Input:
~~~
za
~~~

Result:
~~~
za
~~~

### 0x7b starting a token

Input:
~~~
{a
~~~

Expects Parse Error
~~~
>>{a<<
  ^ (0x7b) Unexpected start character in Bare Item: '{' (\u007b)
~~~


### 0x7c starting a token

Input:
~~~
|a
~~~

Expects Parse Error
~~~
>>|a<<
  ^ (0x7c) Unexpected start character in Bare Item: '|' (\u007c)
~~~


### 0x7d starting a token

Input:
~~~
}a
~~~

Expects Parse Error
~~~
>>}a<<
  ^ (0x7d) Unexpected start character in Bare Item: '}' (\u007d)
~~~


### 0x7e starting a token

Input:
~~~
~a
~~~

Expects Parse Error
~~~
>>~a<<
  ^ (0x7e) Unexpected start character in Bare Item: '~' (\u007e)
~~~


### 0x7f starting a token

Input:
~~~
a
~~~

Expects Parse Error
~~~
>>a<<
  ^ (0x7f) Unexpected start character in Bare Item: '' (\u007f)
~~~


