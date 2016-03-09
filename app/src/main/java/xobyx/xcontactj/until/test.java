package xobyx.xcontactj.until;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by xobyx on 8/11/2015.
 * For xobyx.xcontactj.until/XContactj
 */
public class test extends AppCompatActivity {

    private static final String m;

    static {

        System.loadLibrary("xobyx");
        m =     "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "EMAIL;PREF:b3er@yandex.ru\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "URL:http://www.google.com/profiles/104127734348463196732\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=B5=D9=84=D8=A7=D8=AD;=D8=B3=D9=81=D9=8A=D8=A7=D9=86;;;\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=B3=D9=81=D9=8A=D8=A7=D9=86=20=D8=B5=D9=84=D8=A7=D8=AD\n" +
                "TEL;X-MTN:+249998602001\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=B9=D8=A8=D8=AF=D8=A7=D9=84=D8=B1=D8=A7=D8=B2=D9=82;=D8=A3=D9=85=D9=8A=D8=B1;;;\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=A3=D9=85=D9=8A=D8=B1=20=D8=B9=D8=A8=D8=AF=D8=A7=D9=84=D8=B1=D8=A7=\n" +
                "=D8=B2=D9=82\n" +
                "TEL;X-ZAIN:+249911881861\n" +
                "TEL;X-SUDANI:012-908-4526\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D9=85=D9=84=D9=8A=D8=B7;=D8=B9=D8=A8=D8=AF=D8=A7=D9=84=D9=84=D9=87;;;\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=B9=D8=A8=D8=AF=D8=A7=D9=84=D9=84=D9=87=20=D9=85=D9=84=D9=8A=D8=B7=\n" +
                "\n" +
                "TEL;X-ZAIN:+249911910461\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N:;Tester;;;\n" +
                "FN:Tester\n" +
                "TEL;X-ZAIN:091-301-1221\n" +
                "PHOTO;ENCODING=BASE64;JPEG:/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwM\n" +
                " EAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/\n" +
                " 2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUF\n" +
                " BQUFBQUFBQUFBQUFBT/wAARCABgAGADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAA\n" +
                " ECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaE\n" +
                " II0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZn\n" +
                " aGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJy\n" +
                " tLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAw\n" +
                " QFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobH\n" +
                " BCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hp\n" +
                " anN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0\n" +
                " tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD8qqKKKACiiigAooooAK\n" +
                " KKKACiir+iaJd+IL9LOzj3yNyWP3UXPLMewGR/Lrimk5OyJlKMIuUnZIND0O78Q6hHZ2ce+Vu\n" +
                " Sx+6i92Y9gP8A63Wus+JHwxfwJpmiXpmd49RWQKsoAYlNuXGOitvGAcnjqcivoT4N/By00PTo\n" +
                " rq6i3xth8SLhrluzMOyDsvfr0J3ct+2Of3HhMf7V0f8A0VXvSy72OElWqfFpb70fCU+Ivreb0\n" +
                " sFh/g1u+9ot/mv66fNNFFFeAfem1omnW93au80e9g+AdxHGB6Vof2JZf88f/H2/xqv4b/48pP\n" +
                " 8Arof5Cur8M+G7rxNeCGAFY92zzNu7LHoqjueR/nGahCVSXLBXZ59etGipTnKyRz0Xh+0lPEH\n" +
                " Hrub/ABq/B4QsnHMBP/A2/wAa9b174G6h4c0uXUlurY20Sh3hllCyLnAxuwFY5J4B9hk9eest\n" +
                " MyOlbVcPUoS5aiszz8NmNHGQdTDz5lt8zj08E6eRzbH/AL7b/GlbwTp4BxbH/vtv8a9CTScIT\n" +
                " s6VFb6U+sQ3H2Mq0luC80ZOGWMEZYZ4OAc49qmFKVR8sVdmlTFqlHnqSsjzebwhZIDiDn/fb/\n" +
                " GrOjXtz4VLjT/LgDOGYtEkm4joDuByPY8cnjk16F4j0O2tILAxRNHPPCZ5FzlApYhMZJOSFzz\n" +
                " 2Irj7+x4binOEqE7J2a7BSrQxlK7V4vo/Jm4Pjl43/wCg3/5KQf8AxFcZ8TPG+t+NLSybWb77\n" +
                " YbV2EP7qNNu4fN91RnOxevpULoY2KmsnxH/x4p/10H8mpSxNea5ZzbXm2GHy/B0KqqUqMYyXV\n" +
                " RSf3pGTon2L+1bb+0Q5st/7wJ1x/PGcZxzjOOa+tbX4V+F/HvwsMemLZzXk1sxtrqIqq2s5w2\n" +
                " wYB2DcAGwNxBPqMfHvSvQPhN8W9R+G2rh0LXGmSn/SbIt8rj+8vow9fwrvwFelTk6daN4y0uc\n" +
                " We4DFYqnGtg5tTg7pdHb9fwezMnTNLu7C5uNJnQW99HeNaujHISQEKQSM9D6V9OfDjS9P+Hng\n" +
                " uTxDqJcxQIY4AF3O2WwSoJ+87EgdABnnB4Nb+Hul/FG+07xn4eu0illixNFIu0SMVwGfAJDhT\n" +
                " gjnI2kdPmx/jjfPa3eheG48ixtLVLjLHJdvmjXPHUBG+u8+lejSw/8AZ0ald6/y+d9v+CfK4n\n" +
                " HrPqlHBL3W9ai2atuvnsvXXqc3r3inUvHOqveX8rRWoP8Ao9irny4V5xx/E3Jyx5OewAAl8JP\n" +
                " HceIksrpN1tLJHHwPmGSQMHtzjPtWN4ZDX/iW1sQQm8hVYj7rFlUH9a7zQrjTNfkF/aWiW506\n" +
                " /h2XIJL3EPnbMvnvkBunGSBjnPNhKEsTUVepK+uz69zvzDEU8DQlhaEGkkrNdG3p56tb7jU0i\n" +
                " /mgt8xJbxXg2W9xIwCM5BKqe4zjg+461l6ZbT6ZqOj3ttast7LLNAwIKiQKYwFI6ZPmSKT149\n" +
                " jWndPqWjp4llkfazXaeW7ZyXFxuTZ/ux7jjsAO1a1xA9xr+k6jDbu1qbZbox7zhZmZ5P8A0Ir\n" +
                " z6AV6So4fDLm2atv6/wDA/E8f6zjMY+TSSd7W0T93rr5r5q+lzJ8X6RHHfme3ZZLSVFWFkJIA\n" +
                " QBCuT3G39a4LU7QAMcV3MWlz6PprWJnLWok80IwHDYxnPXpXLaiglkaOMGRyeFUZNfO4ica1V\n" +
                " zgrX1t5vf8A4B9jgaU8Nh40qkr8ul+6WzfnbfzOD1CDaSR2rnPEf/Hin/XQfyauu1ZChkVgVI\n" +
                " OCCMEGuQ8Rf8eCf9dB/I1xPc9yk7tM52gdeKK2/Bmg/wDCQa9Bbsu63T97N/uA9OoPJwOORnP\n" +
                " aqjFzaijrqVI0oOctkfUn7NUD6b4SuvtEjCFYoZC8vyqpIdmHPoCM/QVL8cfBeo3+oW2u2UTX\n" +
                " UMNuLeeKNSXjAZmD47j5jn0xnvxwnxc1Y6L4a0/wZAxWabGoaoFbkMf9XCQG4wAGKkdoyOtVP\n" +
                " AHx517wlbJp+owjXdOjBCGaQrcRgkcb+dyj5uCCeQMgACvqKmJw6h9RraJJa9n6eR+U0cBjp1\n" +
                " 3nOEs3Jv3XpeO10/O11tpZ+RlWNzc6TqttqdkI5JoWDbJOjYYHH5iu+n1i01izlTTIZdOe9lW\n" +
                " a8MxRucltiY/h3HOeDxW/bfGXwDrJl+1aXcWbZ3FprNcuTnPMZJ/PFTSfEzwFaGNrXS574tkE\n" +
                " RWowv1EjAflmopUo0qbhDERs/v8Alrc2r4uriK0alXAT5193lfo7PVHLav8AbLuSGSfUJr4K5\n" +
                " cxzMSmSTnCjgdSOMYHA4rtbKbU9TgC2OlSqjjKMV2xhQMABjgcAAVQf4tW8LS/2H4city6YWe\n" +
                " fahzk43Ig5HQ/eH9axdT8Z+INYJa51R7SPIYQWP7pVOMfeHzEHrgsRWM5YaKcalSU+vz9WdFK\n" +
                " GY1WpUaEKNla77PX4Vpv3saGvadDpSNLr2qwW7lMiytz5twWwSBsXGAcH5i23pk81x2r+KI4Q\n" +
                " 0WiWP2WEkMbm8VXlY8Zwg+RQeRg7j05FQSrb2yEIoGO+KwtRvAd3pXBPFaWoxUV+P3/5HtUcv\n" +
                " u+bEzdR9npH7v8ANsxdXuZJyzyyvLJgDc7EngYHP0ArkfEX/Hgn/XQfyNb97N5jkVg+I/8Ajx\n" +
                " T/AK6D+TV5rd2fR0lZpI5yvfP2YbLT2+33+oFIYbAvePMTtAEYjILH+6u4t9fxB8Drb0bxlqm\n" +
                " g6NqumWc3l2uowmCbGQwUshbBBB5CBSDkFSwxzXXhayoVVUkr2MM0wk8bhZYeDtzW18up0ep+\n" +
                " KZ/Gmv6xrdyCkl5dM6xkg+WgACJkAZ2qFXOOcZqEHByK5Sy1aawiaONUYFt2WBz/ADqx/wAJH\n" +
                " c/3IvyP+NcspOcnKW7OmOHVKKp01aKVl6I7Oyul6EDI7Vu2l8qjoK8vHiS6ByEiB9gf8a+kvg\n" +
                " L8HtM+L+nWEcFzfS6zeXEVrHb28scatI+FVcuvHz7hknHHXvXTh6E8RLkhv9x5mYYmnl9L2tZ\n" +
                " OzdtFfVnIRaqAOtK+rcHmvoix/ZF8O+I/ENvovhbXtQ1m6dFQvd3Frp6Szl9myAzEGQElSmQj\n" +
                " sGOY1wauad+w5Lfr4dmnfUbCz127tbO1nuL23DA3DusLvEEMio3lSFWK4YISM8Z7Hl1ZK7aXz\n" +
                " /Q8GGfYOcrQjJ+ajdfftp1Plm71PdnmsO9v87gOTX1Zc/sZWqW+pXD6hKLaxz5lwut2BjlIRX\n" +
                " ZYX6TsodNyxFipdQQCwBZd/sPQWUmvrc3F1AdDdI9QMus2KiKRlkZY1J/1kmIpP3abmBQgjIx\n" +
                " T/s2v0a+9FR4gwa05Z/8AgL6X/wAn9x8hk5JJ71leI/8AjxT/AK6D+TV9c6/+yXonhm4W31K8\n" +
                " 1G3n8vzXiW+tpHh5IKyhUJicFTujfa69wK+Zvi+nhGwvrfT/AAnqN3qqxM5urmdlaPcDtVY2C\n" +
                " ru/iJIyDlcHrXPWwFXDw9pUat6npZfnOHx9f2NGMrre8WkvXt5HnlFFFcB9UFFFFABXq37Pfx\n" +
                " vvvg14xsdQhcrbx3Ec+5UVzE6spEgVgQ2CoOD6e5B8porWlVlRmpx3Ry4rDU8XSlRqrR/1dea\n" +
                " P1D8PfGa81SzudT0a60uSG/eG4juNOsoIEgniV0jmiWJVVJUWWVQcZUvu4kRHVbnxxq15HZrN\n" +
                " LFIbaSCT54UZZfIhSGBZYyNkgjjQqoZT/rJM53nP5s+FPHWueC7w3OjajNZSHG4I2UfGcbkOV\n" +
                " bGT1BruY/2nPHaWUsB1CCSV2BW5a0j8xBxwAAFxx3U9a+ppZthuW9SFn5I/K8VwjmPO1QrqUO\n" +
                " nM2nr6J/efoHY/Gbxfp8bpb6nEge7jvR/oNuSkkbQMiofLykam1t8RLhAIUG3AxXnHjX9q60+\n" +
                " GkOuW0+p2JutVVUu9K03TLXf8sTxAqqoFt28uaQbl2MwdjySa+IPEXxl8Z+KYPI1DX7poCjRt\n" +
                " FAVgR1bqHWMKGH+9n9a4wsWPJzXPWzamk1QprXul+R6OC4SrtqWOxDdukW+vm+/XTq9T2b4/f\n" +
                " tW+Nfj/AH0A1i6Sy0i1tYrK2020ijiRIYyxRG8tEDAFjgYAGF4yM14xRRXztSpKo+abufomHw\n" +
                " 1LCw5KUbL8/Nvdvzep/9k=\n" +
                "\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:;=D9=85=D8=AC=D8=A7=D9=87=D8=AF;;;\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D9=85=D8=AC=D8=A7=D9=87=D8=AF=20\n" +
                "TEL;X-SUDANI:011-917-4097\n" +
                "TEL;X-ZAIN:090-299-7578\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:;=D8=A7=D9=84=D9=85=D8=B4=D9=83=D8=A7=D8=A9;;;\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=A7=D9=84=D9=85=D8=B4=D9=83=D8=A7=D8=A9\n" +
                "TEL;X-MTN:092-641-2556\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=A7=D9=84=D8=A7=D9=85=D9=8A=D9=86;=D9=85=D9=86=D8=A7=D8=B6=D9=84;;;\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D9=85=D9=86=D8=A7=D8=B6=D9=84=20=D8=A7=D9=84=D8=A7=D9=85=D9=8A=D9=86=\n" +
                "\n" +
                "TEL;X-SUDANI:011-600-0840\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N:;Hose;;;\n" +
                "FN:Hose\n" +
                "TEL;X-ZAIN;PREF:091-509-4573\n" +
                "PHOTO;ENCODING=BASE64;JPEG:/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwM\n" +
                " EAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/\n" +
                " 2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUF\n" +
                " BQUFBQUFBQUFBQUFBT/wAARCABgAGADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAA\n" +
                " ECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaE\n" +
                " II0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZn\n" +
                " aGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJy\n" +
                " tLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAw\n" +
                " QFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobH\n" +
                " BCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hp\n" +
                " anN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0\n" +
                " tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD2r4OfGPR/2mh4q0CbQP\n" +
                " EHhb+wFtjdLeSi1uJWn83y2hmgk3pt8ljuUqclcHg59uvNaTQ4VOosGtgcfamwuwYPL9uo5Iw\n" +
                " OegxXyl8K9b8UX3xk8Uan4QhstP0C901LZItQgz/aNxHLGsU21SGWNFklG4HB8w8HCmvbfH3w\n" +
                " l8T/ABO8BnRtS8Zf2ZcuoW6utFtPs8Vym47k2M8jxqynGVl3DGQ3as5RSdjnhK8bou+O5tS+K\n" +
                " fgK707wPqFitprFtLbDxIHE0NqhzG8sQXImkXnamQpKnc64Abebwhq2oeGtOTUvFOoJqFoiLc\n" +
                " 3mk28Vut0+0bnEciylAecKGOM4yavfD6S00zwjpmn/ANnQ6CdMt1sDp8BPkW5jGwJExA3R4UF\n" +
                " SQCVKkgEkCp4l+LHhXQZ7uxn1iC+1KKDzjplpIrzlcH5tmflBwQC2AT3oimwlJRV2zzfxD8Qt\n" +
                " Y8MfG/wt8Omv47g6/vmjvrhf3kMUcU0sihBgMxWAqGyApcEq23a1X4laRa+AvF3hCTV5pdX0r\n" +
                " VJG0vTxcOkYGpSkn5vmVXkmQMFOz5BFKAwExU+YfCzXdR+Mf7Qsnxb1iwmstO0cT6TpFnN5e5\n" +
                " IwXjmcbSe7vgthvmYYA216T+0p8fdI+FtlpEI06z8SeI78tc6LbTLugt2jBCXcjfwohkUfLh3\n" +
                " LkLj5mSpe56GuEw9XMKkaFCLlOTskt2eq+CPhDb+Db7UdY0q9aK5vo1jntmVPIk2sSpyRvBGS\n" +
                " PvY5PHTG7Fe2fiPw5f28vl3NncW0sMsO/O5SrBlP1zj8a/Lr4o/HP4xa5KNdHxH1WxOmtJcnT\n" +
                " 9PkFjaRpkuE8qMDzFGAB5zSHbwSxLZ9m/ZWvvtfhnx7rPiq2bTLa5ub3VNQW6nZoYUmZ5p8kn\n" +
                " bswzZYAA1jTnzuSas13PWzrJMw4frrDY+lyOSutU018m+p9PfBD4V2umfBv4faLrtnFDquhab\n" +
                " abxblR5F6IAJnRk6sWaTLg/Nvbsxzw/xnvPFd1Fr3w+13wlBrnhG4sDI3iazv2SVYiWKGW38l\n" +
                " hDImxsS79peIOBGWCL714J1bSdV8OR6xpWq2mraTKPNjvrK5SeGRAMEq6Ehh8p5FdEimUASAx\n" +
                " SSHdtbPHoOp5xxx35rW6R4TjzJpaHjn7MXg3wh4U+FGl2fhWdtVhjbzbiW8u5Ll4bllBlVRI7\n" +
                " eSCxJ2LhcsWGS5Zrf7TWh/2x8JJ0SyaVjqulRTrbxFy1o+oW8d2sigfNH9nebcGyu3JONuRlf\n" +
                " GLQr34K2Xi/x14L1CDT4bTTbjVtY0a8hMtncvDH5hlREZWjldEZCwO0nYSpIOeo+EfxIvPEvg\n" +
                " PT9S1xUs/E1ysVpd6akewWF2QN8ZUliCpOQSSGUKykhgSuXrcUXZcr3OY+K+i2ugeEm8fapPJ\n" +
                " LqHhUf2xFDayiGNfKVmMQJU4DpujZiD8rHAGeMnxr+3R8JfDRgtdN1aXxfqU9vHcppvh+ESy+\n" +
                " W+eXeQpHGy8bkd1cZHy5Nc/wCLPj54J8VDw58PdW1J7a78TXkMO6+0uVLXUII5Ve4hHmpsZJY\n" +
                " wYyCT/rQMk4z83ftZ/CHw18OPjZDqvh+9UR+LfNv721+UtaSIy7pI2PIjkeQEjkh8AHawEeVT\n" +
                " njG8Vdnt5LhaOPzHD4WvPlp1JKLa310Vt1dvS7Tte9nY+gPBv7Z2kanrmu3d7ol54d0oxrMP7\n" +
                " QMXmsyhvMJ8qRxjYIwOckg8cZqTQ/h94f8Aj98WL/x1Jq7w6ZNpdvp0Wm6fcLsljV5JC8ki5G\n" +
                " XEpGEIIUBt2ThPjxLdGUI8kkg2gHe+ecEZOMA/5NWvCvjHWfhnrsWp+GL86dIzmS401mK293k\n" +
                " DduUcCTlR5igkYAO4DafUq4edKHO9D9kz7wlnh6LrZVVdRLXknbmfpJJL0TS9T9INW+HnhTSN\n" +
                " Ea3SwtdMtbeIoslqoiMK/wCzgcDr/WvzH03U5dTm1O+e4kePVbya9jWVNi/vMYYR72VSUjUlQ\n" +
                " zc5G5gAT9BfE79o1vH3wP197XxFp1pdXlv/AGcvh67gAvZ3dhDOCxlO7Ys6PtVP4QdxDgj560\n" +
                " S5iWG1hSYB2IQKCu0Efw59cc4zzj2ry3Vi69Om9ndnF4U4HDRzLFY7FpRdGKS5tOVybUnrazV\n" +
                " uXv71i5Np8N3atbEGKNgy5jyGUHIIBB44/wA5r6E8F6rDo/w30jxKXhh0yx1O0XXIrnO6Swkc\n" +
                " JwBwXSVonOTjYsvBJUV84a3qv2PSp54Fl1J1iaaG2svvsyhiqHGSCSBng8EDk5z9FfA/4X6j4\n" +
                " 38ATabdarHZ3d8UkZNpkhjG5SysmV8zClsZIG4KcAjjvrTg6qlFWVv62PS8WsTgMdDDVMJLmq\n" +
                " LmV0tGnZ/Fazs1pZvdn0p4p8D+AhqWheJzpOmQ32n30F4mqLAqTQojhiQ4GcbQeB1HHQ16tP4\n" +
                " gBsob/TtL1DVBIUdY4YhEwUkZJWQqRgckfe4IwTxXzjc/sc+JfDX/AAh8ngn4iav/AMI7pN+L\n" +
                " m68P6sIXEluvzpBBKIwUTcqp5b5GxztZNoB+jfDV4bjTFDq0UqZVkPBVgeQR6jHNYO0z+eIQl\n" +
                " BPoeFePfipp/wASvA+qr4M1ey8S31oItbe40+4LWrpbziVIDOmQSxjOQN3y7twUMoOBr974O+\n" +
                " NcugeLfCevx2PivSpY3guI3eOOfy5Vc2twFKsUMkSh4yw6EEcV6ZoXw40vwp8VPFc1vFElrro\n" +
                " XUJIRCFjEkpdZRnJ3bmVnPTmTp0rjP2ff2b/C/hi98V6re6Jby32qazdF3vlEqrGk8kUQiQjC\n" +
                " 70AYkDcxfBJCoquVktDioSqSqyU/6t/mbGh/CPS9R0t7PUEF34cjkhl0/TpAxeExtuSQylt5O\n" +
                " 5VZcEFT36Bb/iD4Uaff6XrkNhZR21/qentp/wBpbc7FdshUOTn5VLuRxwWPrVdfiJYeEfF9j4\n" +
                " PlttUuL2a0kuYnS0MsUUSOqhXkUYVjv4zwdjZOcZ9Cs9Zs71Vm3hGXjbgZHb1xScr3i+p6ML0\n" +
                " nGdN2cXdeqPzC1nQdZ8NSww61Yyad5svkxeeD++YKzYVsYJwjHKkjC5BINUJl8xWhxv3bkdnP\n" +
                " KtnGBwQ38XPT65zX0h+3DqRvdN0C90pS3h621WexvLxflia98tXEYGR5pUJKNy7lVzIjEOm0/\n" +
                " JV94vttOUgkyE/c2njAz8xOenB/znFvHb05vb8fP+kf2lw3xDWzvLo5njaygtU4RXVO3vN8z1\n" +
                " 3SilZPd7nOeKfh75uorqdvIkk0aJvAUAF0JIIGNvCbzg4+7kHqK9Qi0hdPtbby42+zTRCeGUh\n" +
                " v3qMBhgWJJBweSScggnIOOLfWJr3WLSzT91uCq+8nbyS2Af8AcOM+/tXf3vgvWNAgtntidRtP\n" +
                " Ia78uOMloYlK+YHI4wN2V78HjCknXBRw0GpWv2PxOHGlHhbjnHNU19SqShGdtZRkopufdpScu\n" +
                " ZdnfdWecCELZYEg5bnhe/8Ak17B+yv4jn0j4kNp0t5utp33xQkcRqVXK8DuyO2fUmvHLa+tNQ\n" +
                " QTQyCWEnA28HqfxHT/ADxXefBf4P3nxeGoaXLcT6VPcKLnTNTs2MT2sgw0bpJtJB8wEkjJCvx\n" +
                " 6UYqUXJRjr5+X9WP0/wAVcdhK/D9Bwmp884yg073XK7tW3VmtfNH6ban8R/D/AIN8M3Gpa/qF\n" +
                " ppWl20YklvL2URQxgkKuWYgcsQoHckDqazfDesS6xFcXrRGAzMzrE2AVXOFB7ZxjJ9ea8C1v4\n" +
                " YeCPHv7ONtaXR1HxPFdaeJtKu9V1J7i8FzIMwvHPcOdshkZAMtt5VfujA7Dwh8NPiL4e+GNro\n" +
                " zeKLSa+htHjbVJrUeYmRgNEpyq7eSvmCQcDcH5FYRhY/kNV1KTVjp9b8K+LvE/xR0q/s9VsbD\n" +
                " wnp9k8V3bvatJd3Nw7ZG19wVUVQmDhuWYY7jzpdN+NHw2+Jni6fSrHwl4j8O3kEV3aLdX0+ny\n" +
                " rMFZZXbbbzAkhVUrvxiNCCCzCvdvhvpum+HfAujWFndTX8ENqhN9dEGe7cgFp5SFXdJISXZto\n" +
                " 3M5OBmuF8a/E/wxrep6j4atvEFu+siMWjadp0vnX8YkaMPIIFDNtVXRt5XaMgng8zutR+zhGX\n" +
                " Ot2cl4o0LTvgPruq/EnXdU1bWl8Q3GmaPcaasSy/Zz5pig+xwom98yXDM8eXdgxK5KhG2viX4\n" +
                " ef4g+G102ytrOwBMF2bm8t3mljdJUkVPI+Q4YBgSxBH905ONS5vrtviLFo+sWYnt9OtBdaRe/\n" +
                " LL50zs6TDy8FkkijKDd0ZblsfxBczxL4st/DvijQtHS3un13W5JYrEzW0yW8rpC8zrJMEZUOx\n" +
                " HIDctg7QSpw13ZvLsc9pHg/Tda+Guq/CrxDBe6h9pN1Ol9HCi58yd50Zdo2o0bOoXcMHYpOSS\n" +
                " K/Ov4qfATxN8OdduLLV9MnWCORm+0bGMDwhiFlDjI5AJKk7lBBYDNfqL8NLHx7a/ELxO3i+z0\n" +
                " 2LSZbazl0qbTXeROXuRIjM6qxkVVhLHG3DqV5LBey+Inwz034heFr61mBt7tkIiuo/vwvg7XH\n" +
                " 0OOOhHB4JB8/EYaOI62fc+oyPPsTkrlyRU4PeL2v3VtnbR91vqk1+VvwE+DN9488eRR6lALu3\n" +
                " 4ultrmBiUB+WPOeMYD/ACkZHGcHiv0r8XaCPDXwdu9NtIo4dQmtvslhIsSMYblwFjl2sygrG3\n" +
                " 7xgDnajYBIAPCeGPhxqPg+E6h4VS11W/ji/dpqL+TJK2csDKilSGAAAKjDDlsN8vUeAPE1/wD\n" +
                " GbVLqDV7V9NtNGnEHkW9xuFw7xRybmOFYbVkXI2gglgCR17oRVOCiumh8xia08XXqV6nx1JOT\n" +
                " fm3d/wDAPL9R/Zo8IeF7eG+1xINb11ys+oamYI4DLNwSyKAcAEDC5OABlmOWPrnw++Eyafd+H\n" +
                " tU0RrKDw+1rPJK7o4nkdngMBRMAFCqzEsW6+XhXDEr5x8ZPAnjTRvjD4Vn8P6VfeOvBbWNzFe\n" +
                " aZHcWyy6fP5kTQysJpkWRdq7UAXegVjuPmYHvWhfFC0u4rSDxLp9x4K1C4lS1gtNZkhRZ5GKK\n" +
                " iRyxyPGWZ5AioWDsVbCEclNW1RKk5e5LZbHnfxe+Cd9qmh2V/pXiaZLbQ9ZHiC60u+tofKuII\n" +
                " pHmFvGY1TbsbY0Zbd/qlDHJ3jv7vxDbweHr3UHuFNrDCzliRjAB9P5UeO/Dlj48tL/wXcajLD\n" +
                " 9pWG5vksLvyrhLcS7kDFWDqsrRMh4wyrKua47XPA3iHwre6LFaofEfhMXKfb0aRUvoFA+ST+F\n" +
                " ZUWQKzjIbYpAWQnadlZ6s5Jxe0FubHwOn0yX4S+GhdabdaZqcVkg1BNQ02Wzc3IAM7skiJyZN\n" +
                " 7bsYbJYEggnn/AAjrvgDxB8XfGD6CbH+27D7JbazNDAyKWHm+WDIVCOw5U7SxXaFbBAA9b0W/\n" +
                " tJrTaNrqnyyMQCPbj05/Ie9fKljq3h+zXWY/Eehv4F0Cy1HULbS9UhnSztorNZpVjeK4iaM2i\n" +
                " mJF4Yx4DKgLHgyveuazl7JLqem/DnUvH1v4s8WweIbXSJNMgMMNnqluZBcXL7Azq8WfkRQy4w\n" +
                " +cluueOh8Ya1qNu+iQWOk+bcXVyLaO8J3rbfupZGllAA+TEbLgHLM6rld24aM+uaZaagPOuoy\n" +
                " NQkX7L5JDG4fYcqmOSdqbuOwJ6Cqd7400rR4mn8SzHTM3KW9ukcUkoG9lRASoJyWYZOAB+Gay\n" +
                " jrLU6G1bQuap4jTw1a3msalc+Rpun2zS3Erg7IokBZn4zwBk8ckDvirjatrHiPTtPvdFhQabP\n" +
                " Ji4e5Z4JUhMbkPGhX5m3iMFW24UuckqFbkPHfizw3Np7eHJbf8Ataz1dhZ6g09rvtbe0fIlM5\n" +
                " bACMuUwc8sMrtDleo0zxtp3jbwrqlp4D17TtVlETwxarZkXdlbS4IUEowWRlYfNGHBAxnbuXN\n" +
                " NJNAndNGDqWrP4g8Ptp/hPVtJMl5HDO17buLjyrWTcRKqxsd+/YVXlc/MwJ2EGHw58I9K+G8n\n" +
                " iPxfo82ot4ju4JJLl7m+neCRgCyB7cMIzt4UMqh9v8ROSeG/ZM+Ht98L38UeG/EKaePEGmzxE\n" +
                " XFkOL6yZCLW4KkZQsyXIKEnDRvgkYZvoS0vIFsmmuMRSOzcA8cMxHP0q/QxUdLs8t+DUWu6hq\n" +
                " /iXxAtrbXWm67fC/iuPP8Anf8AdRxDACnKhIkCk8nHoQT30CyeM2vR4p8Npb6dp99EbGw1COK\n" +
                " Qzyx7JUugVZl+WTbsBAZGiLddu3x/w9+1FofwwKWXiayisvDU1/d22m61oyNLYxxCfFtDLtGI\n" +
                " XMboFydrGN+QSqnK+KX7SOpeL/ip4d8MfCe806eS1tZLjXL3VrKSe1jjZ1EIURyxu0nySn7wT\n" +
                " DcknAByylsiFUhBas9p1CWw0X4jaTcLpbw3mtwtps01jYg5EKyTxNcSKCVjT98qsx277kL1fj\n" +
                " tIGHmfKSQegrzzw34e1p/DVz4i8QXkOua/cOlxBHaw+TDbQJtKwRAlyAzIXYszElgDkIoHEfG\n" +
                " n9sfwP8H7rw9pN1Pt8T67cxW8dlOViWwhdwkl3dMzKqRRkn+LLlSFyAzrOux0XVrknjbwp43g\n" +
                " +LF3ceEykngqLTZbzUNDtblEuL3VG5SNBIoWJCoViBJGGeTc2NzlpfgT4Z0n4n/DDS9W8Y+E7\n" +
                " Oz1NRPp2qaTc7biMXcMslvcK2WdXHmxSY5IwQeTXoHw68U+HPENld6jpXiaw1qGznaC9n0+5j\n" +
                " mjjnUKzxyMjEBgGBK8HDDoCK8g8NfHyfwx8ZvGvg/xi8aXOo31rqfhi1s18yS4sp40gCpEu5/\n" +
                " lngneRz8qhmclUGQO9tDOCX2jgfhhe+L/AIy/GTTPE3h/Q30/wJ4fuZ/P1XVImtlvZvs8sOy3\n" +
                " QjfIA0oPmEeX8pAJINfQvjD4QW/jzSZIdU1m4iZiJI308rF5TqdyspIOSGGfmyPat/QdOj0PQ\n" +
                " tOtLZFWOGL5ii7UkYn5zjsS+T/U1a1W5g8PbLwKZrqcFYIDId8jY6Dk/LnknHH6Ek25XRUIKM\n" +
                " bP5nhPgvwZeeCvCOp6JruqLquuSzm3vdZeIRrIrEhZAu5tihGBCbvl5ySSzH2vwr4Y07wToll\n" +
                " o2jWUWn6ZaqVit4l2jJYszH1ZmLMWPJZiTyTXy54h17X/AIf/AB6g8La3Fea1Z+N45r2wnhQy\n" +
                " S293GCZoQc4WHyyhTcTt2OCSCoHo3hH4s+LPCuspoPi3RL+30+WNv7P1+6SKZSy72ZLlonxGd\n" +
                " uNjuqq23aWLlQzmmyadoXTK3xAfXfG37Ynw70jQL2607RvDWn3t/wCI54ZnWG5jmQRQ2zgKUc\n" +
                " 79sgVz0EhXDR85XjK78T+N/wBpuPw14f8AiC2m+EdE06CfX9DtrW2kmuHmecRQrIYjKhZU+Yh\n" +
                " x8uNoBYtXc+FEtfhT4a1fWNU1tfEPibxLqmy2uZoFQbri48u0tR5MZYRo0gBZt5XdI5OM49O8\n" +
                " HeBNO8LafIkIYzXMjXM9w4G+aZvvSvgAFjx7AAAAAACU9DRpswfFXw48OfEzwHqHhS8ja10a6\n" +
                " spbOT7GqxGFT8rKpKnHHG0qR1yK+ZtS/Z61D4AeLoviN4E0iTUfDl3Zqmu+HrVF+0IABtuIF4\n" +
                " 3so4ZM5Zc4yyqre9/GK1udLtItYl1h9L0m3uIhqdhFbmU6ijusSruU7lUF8tgMGVcHC7s6us/\n" +
                " FDw/daGukQ6rb3GoX6GPbZzJLImWCsQgJbjcTnGBt+bHGdU2jB04yVmY/hzVfGHxG+HFpe+EN\n" +
                " VsNHtNVs0uNOvb7S2umtopI/klCedGCx3I6q4IGCGBzgYfwZ1/UPANro/wAPPiJqDeJfipKlz\n" +
                " qV5cWFtNJbzwPdTeXO0gQRxIFCoVO3awCKCGTd7hpf2PQtEiZZYLTT7eFRnKpHEgGOOgArxrW\n" +
                " 76KT4qN400iF9ViNqumlo1aRfIyGbYQCM7gT6HB5Gc1knzGijybM7TxJ4OPimKS7sZYIdWgIa\n" +
                " 3n2BYpCu7ajEAsU+ZsHnBOQD0PyR8crDx3beIvhB8crLQjLpXhTUJF1yCMLLcx6ZcFFlmKgnK\n" +
                " RKJGOC23zS3yqHI9Z+Nf7ZWhfC0S6W1jrM/iO4sGvLOyXSpssASAAxXaDkdWIX/aNd3+zX4ks\n" +
                " fiR+zr4JuZtl5DqGiw2l5DJGcGZE8q4iZW64kSRDnIO08kUapalJRctD//Z\n" +
                "\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D9=85=D8=A7=D8=AC=D8=AF;=D8=A3=D8=A8=D9=8A;;;\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=A3=D8=A8=D9=8A=20=D9=85=D8=A7=D8=AC=D8=AF\n" +
                "TEL;X-MTN:092-584-2548\n" +
                "TEL;X-ZAIN:091-301-1221\n" +
                "TEL;X-SUDANI:012-650-8700\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:;=D8=A3=D9=85=D9=8A=D9=86;;;\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=D8=A3=D9=85=D9=8A=D9=86\n" +
                "TEL;X-MTN:+249969905775\n" +
                "TEL;X-SUDANI:012-870-3878\n" +
                "TEL;X-ZAIN:091-555-5495\n" +
                "EMAIL;HOME:ameen19880000@gmail.com\n" +
                "PHOTO;ENCODING=BASE64;JPEG:/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwM\n" +
                " EAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/\n" +
                " 2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUF\n" +
                " BQUFBQUFBQUFBQUFBT/wAARCABgAGADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAA\n" +
                " ECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaE\n" +
                " II0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZn\n" +
                " aGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJy\n" +
                " tLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAw\n" +
                " QFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobH\n" +
                " BCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hp\n" +
                " anN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0\n" +
                " tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD420CxhDKwmimkwWjVJA\n" +
                " CrdOeR61tWHgua5kkF3II/mA4P3vocEV3UPwE057d3t9atC6/dSSORWPPr0x+P4VPH8KfEdta\n" +
                " SJb3drcRMRu8u4Bkbr27fgB159vha0a0k1CVvke3QjSTvJHBTeE9H84RLfEAHBJdWJ68ZAAH6\n" +
                " 9aW28G2NpdmWLU5oioba8P7tsc8buevIziuh17wRrGk2JvNR0C5urOFS8s077xnjLAqQwAA7k\n" +
                " j16cYtsLdLBp5NBnWCZMrJGJ3i74HLDIOOuT9K86VCul8b/AAPVhKh0gaei+TZqu7VLp44yXK\n" +
                " zsrDGcckjOM9xW4dc0+K38/f54AJ2luuBzgntn37/hXLf8LD0ixjBn8MW8hUYkmYICwxgtjac\n" +
                " 5OeM9+tVpfjFdyW4XQNDjtRgCSUIBETk9MDnrnr7epPlzy+dR3s/wSO2GJhFW/wA2dFYa7Yar\n" +
                " eGK4sp/Jk4kdFBCD1bP8OOfXj86GrWOh6hFPDHeW0Ny4EayJMSpPTv17j/IrjZPEfjG4ZpTqA\n" +
                " B5O2MlevUcAccDiorPWJoJc6jYo0rKQokRdrMTnO4DOfx/KtVgpQ1hK1uzv+aKdX+aL+a0Fbw\n" +
                " 19k8jY7XEkB2h4lEkJBJI5GCPvHsc46ioPDzCw1G6B/wCPKWR422AlT+O4ce+eAc1oW1m2qO8\n" +
                " McH7xfm3RykKFA6AHqTx36jHNRJbw26ExQIZDwkhCnOPY9K7FNtOMnds5pcujSPRfhjqiWXii\n" +
                " ydLouVlASJsktu+X16jP8+fX7Y0tC0IY8kj/AOvXwD4KvVsdasppQxZJUkwR97DZ5J98/nX37\n" +
                " 4YZpNMtGc5Zo1Ld+cU0uQzTTbseVwm21KFh5UNzGDtYOOM/iDTrjSdPljCNZqFHaE7P5EVHZ6\n" +
                " VPao4Sc7jhh8oC8dMAeveo9Zlu44ZngdEULu3SDaR6gZ47D8zXc6korXUxhSjI8d+J/jbTNP1\n" +
                " OTR7Y3kFvHFvfYryJcPuGEPJ+Xggk564wa2vhB4ssdde50bTtWZHRPtYSez+RAzZdPmPJDMOe\n" +
                " M5OBxXh/i/xdZ6jqt/cR4BZwiPypIBLsSMjHIAHT9DXrPwN1C2vfEF99ijha22qkSq+GQE7WJ\n" +
                " A4ziFDgcYHua0qSUIc0kx0ouU+VGV+0JqlvLrun+HXFvdpCgu7uSGMRsxOQsZI5AxhiPdfSvO\n" +
                " UmluSqDCQoNscS8Kq+gHar3j26luPG3ia7ciaVb14UY8AqpKr+GFFczFeXcQMv2hH2nmNFxj8\n" +
                " acrunyxZtRShNzkjttJ05nbaq5BGTW5e+FIrvTzHPAGQjGSPy+lYHhzUJ7zTJp4yA6Lg464x2\n" +
                " pNJ14XVzJHcarcRgHhS4AJ54wT9f1rynTlduL2PedSPKk1udZ8N/hHdeKk1BbLW7awvLNgjRX\n" +
                " JaPzEPKncOucEHjtXYf8Mv+IYoI2s73Tr+ZifNSCfJA467kH6E10H7Ocdvc+KLuOW3jvo5rIt\n" +
                " iVAwyki47f7Zr6Nl0fSPKRZbOOAYJCxny+nXGMVnKai9Wef7BO6Pj27+AvjTRnjkm0iSVEBK/\n" +
                " Z5I5nHrnYScfX+tfVnw3E8nhyzF2GjvFQCWORSrK3XBB57j8617Xw5YvbZsp7u1Ujh4Zd56+p\n" +
                " 3Va0vw3Dp1x9pe8nuZACqtckZUE89h7flR7RSVjD2Ps3c8tjtcj5sj2Fcv8AEQCDRJZAW4OMD\n" +
                " uOa7aFOvsa5f4jaTNe+H7kRKHwCzA9xg160VaRxv4WfC+oyI4C7Zdq7h8xYqmd/C98YIOOe+e\n" +
                " K+hP2ctR03TdN8RXtyn2W3tljaW5lXlvmlJbpwduwbRnoPWvCbnSNTebatpDOPLESMhAPO1hx\n" +
                " jBPzqD35r6I+BXhGfxN8NvEljcWot5ryRo1ZskKTGrIeOwLAgV0V42jqyMPK8rI8z+JZ0zXvF\n" +
                " es3ulXXnWF3KkiuoZSTsG7qAQd2a4EeGIYC7Bjluu3qT/kc10dzp9xpr3VpcRPDNE5Do4wVIJ\n" +
                " BH6VlTylQNz4PY981ztuOiZ3wjB6tHWeCNMXyLhWkSMBQPLY4z7fWuq0r4caa8wuZFKkja6nG\n" +
                " UyeRyDwa838OWkUok82WRtxyw8xv0I969N0vVESwjiilLOowxZ8s3XnPevKrRnBtxb1PbpuFS\n" +
                " CUktDtfBmup8P7+4nsLZLoNbPAIw2MKSpOPf5a+lvDFpPqnhXT7kxq7TxrOhnJbYrKCAMc+lf\n" +
                " KvhnQbrWp4ESAuZpRHGxGBvyMAH1x2r7d8OaZHpulWdlGQVtokiH0VQP6V5lWCTUm9SefdLYo\n" +
                " 6bZy2mnujjLBRhdu0D17Dj3qlpL3UcTq++bBGd7hiT3Ax+FdgbM44/Go/spzjBYHqMZpwSvc5\n" +
                " 5S3PE4Qr8A81g/Ej7UnhS9WySaW5lXy0ESbmyc81v2xGMn+VaMKCReRnnpX0rfK7njJXVj4lf\n" +
                " RtX0ydI5La4YW0hKh7M7N2dwO4c8kDnrjFfTH7NllPH4SuDdxlHN0QP3ezKrHGoIyBngdf8K9\n" +
                " Hi0W3uMCWFG5Bwyg81rWdpFaoPLRY1HZRisqtaMlZI2pU3F3Z5P+0D4B0l/AGqalbWcFtepNH\n" +
                " cSSRRqrSnJU7iOv3yfrXxTqtss5CkspB42nHrxX1h+1F8abbTNEn8N2Pl3DzkLdTZyI8MDtGO\n" +
                " +dufr65x8cXXiJVuG8wAr1BrSjTqKPM0RUrU+flTOh0Sa1t2UPZTNJ0yshAP5Gur0aG3t5hJH\n" +
                " 5iuzHMZYkCuG0nxxY2gOUV27Eiup8P66mqXSNFGQgOScYHWorqbeq0O2nXhKNk0z7u/Z58OnS\n" +
                " /AEE06ASXcpuFyMHbgKP/Qc/jXrNqQrLjNec/A3xxonjTwVZJps2ZrGFLa4gcAPGyrtzj0OOD\n" +
                " /XNeqW1oCgwM59q+cknzu52RacdB8TqxIxz79KeUDggYqI2pBJHzdiD+P8AjTPNERKjjsfanG\n" +
                " OuhnI8Ct5weP69K1bORc/ez07V5XrnxV0fQYpCs0d3PHuxHG4+8ATgnoDgHjrXDTftI363TeT\n" +
                " Zw28RkMcQZTI0gAznORjOV9evevqvY1J6pHzv1mnTdmz6ek1C3020luLiURQQoXkkc4CqOpJ9\n" +
                " K8P+J/7Uui6VpxtNEllnuJvkM6pgRj1GSOcZP4V5D8QfiT4k8TaG7DU2eJmdfIiIWJ02t83B+\n" +
                " YAqwyfUcZ6+E6o19qDCMMVKhmO71LEZP4HNdFDAq96ructbMJOLjSVjc8WeM7TxO028us8pJL\n" +
                " uc7m+Yn177R17c+3NR2TzxlXUlh2I5rmrZZZ7xVO7O/jHc56c/UV6HoEserXHBHzSLFlxs3Mf\n" +
                " UE8c/zr0K9NpXgcmFqJz5Zvcr6N4aimkUsM9OCOK7dpItA0qSQIBheBjqakj0z+yXw6qCOMEY\n" +
                " NYXiiSW/fyIBlQMnHSvn7yr1FHofT8scNScup33wD+Kt54A8RW98j7oHG24jPAZMjP0//Uea/\n" +
                " SPwZ4jtPFWg2eqWEvm2t1GJI3yOh7de39K/IG41oabCLdP9Y2PMVedoz0J/vDH5elfVXwL/AG\n" +
                " k9T8GeHtKsMw3umKoRYpcKykk7sN2+Y9wcZ9qvG4F1EpwWp5+DxqhJwm9D71CZABP4jmse98y\n" +
                " KVlxnnHSuQ8HfHfwt4ms1ka9j0+4wN0F24Qg+xJwefT9K6+HUrPV41lt7iOZD0eNgwNeIqcoO\n" +
                " 0lY9lzjNXi7n5R6pLHc20M0shjDxqH5+X7wwT6kEkHb0DDPU1S1C++0WYVJgZYWWARjbvyFIC\n" +
                " tjhsGMEMM9eeorQ1K3/ALMvGkkmZdPKyozQnckLEtsiB4y37ocg5IwMgAGsqWE6n4itVCyQ3o\n" +
                " uI45IGGcSgursGk7MYwcNn7zelfeqLabR8E5a2Z2dzapZ6dlxvjYrA00VwGHlgFd3GQMLHMMD\n" +
                " n5zgntxH2eNLwGZsuXEb8YwR5YZT9Oa6zUtRtZ7y2t4SJLeMlRdWi4WNPuZx0Hyo5JHyjdkck\n" +
                " msW7ZbiSRp7dclfNZ40woyGIK9ycyIMnrmueLte512uZFx4b8i9F2kMbRSoHCcfKipE24e/p9\n" +
                " aoppsthMJrdn87aIpEBwT8zAMCOhBVK7lLaQTmwulZreSVYVI+8VLqqpn6RNS6lbPdWDC3g8q\n" +
                " 5ihFzM6fwp+9k2/iAPyFXGbWhTpq1x+i6Jq3iPw8buBJryOBC7SbclUClucDoNrYPPHH8NVbL\n" +
                " SLzUL2a3ZxDDDE5H7sZdtr4OTnH3R+teieFmm0fTrDT4nax3WMUM6R8B/Mkj3BvX5X/WsGHUF\n" +
                " sLyR9nmHbGzsQMNm3/x3VzLl5pNRVzolKcoKLlojGj+HumWXmNqDm4mecxgY27ct6fXeKrNo5\n" +
                " 8P6eiQSF1edXWHPKnCgj6Hf0+ldRI51nWnlYhVa4ZVHbPnAg/lJWT4mRTp9vd7+oQiPHAYIQw\n" +
                " /OJT+dXG90mzJxSR2OhXrR6fcXs7M7Q4dYxwvO0jOOq5dhXR+DfirqukyJd2l7JbyfIJEjHyf\n" +
                " dXBbsflDHnOME+tcNrdz9h0G2trcPECTtz92RVffx7FTx9KZpamDTo7W3Bdrm4EKSuudqoJCy\n" +
                " Z7NtKDP+0e2an2akndFqbi9Gf//Z\n" +
                "\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N:;Evan;;;\n" +
                "FN:Evan\n" +
                "TEL;X-MTN:099-007-4502\n" +
                "END:VCARD\n";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView y = new TextView(this);
        y.setText(getVcard(m));
        setContentView(y);

    }


    public static native String getVcard(String v);
}
