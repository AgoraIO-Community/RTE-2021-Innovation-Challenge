root = exports ? this
# !!!! Hotpoor root object
root.Hs or= {}
Hs = root.Hs
root.uuid2s = []
$ ->
    root.uuid2 =(len, radix)->
        chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('')
        uuid = []
        i = null
        radix = radix or chars.length

        if len
            # Compact form
            for i in [0..len]
                uuid[i] = chars[0 | Math.random() * radix]
        else
            # rfc4122, version 4 form
            r = null
            # // rfc4122 requires these characters
            uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-'
            uuid[14] = '4'

            # // Fill in random data.  At i==19 set the high bits of clock sequence as
            # // per rfc4122, sec. 4.1.5
            for i in [0..36]
                if !uuid[i]
                    r = 0 | Math.random() * 16
                    uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r]
        return uuid.join('')