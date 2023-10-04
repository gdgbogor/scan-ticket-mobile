package id.gdev.regist

import org.junit.Test

import org.junit.Assert.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun removePrefix(){
        val ticketNumber = "GOOGA23757216"
        val scanResult = "57623:757216"
        val resultTicket = ticketNumber.removePrefix("GOOGA23")
        val resultScan = scanResult.split(":").last()
        println(resultTicket)
        println(resultScan)
        println("Is Equal = ${resultTicket == resultScan}")
    }

    @Test
    fun encrypt(){
        repeat(218) {
            val algorithm = "AES/CBC/PKCS5Padding"
            val key = SecretKeySpec("DroidJamIndonesi".toByteArray(), "AES")
            val iv = IvParameterSpec(ByteArray(16))
            encryptDo(algorithm, "$it", key, iv)
        }
    }

    @Test
    fun decrypt(){
        listOf(
            "kZWFN%2FMEYUT22ZQpYCJaX5K24GA0x%2FbV1xhZUncjeHCNXXa2ud9DdTHmIK4QXGME3y0pI0HKpZcuylUqmq1fdA%3D%3D",
            "I1GFAAEqoPYTHlBh5174LoBJohOAHetnsXNIzijv9nrLu7%2FwNmWmpWxpwPu8p20ZkrYkiXStPro5wiN%2FoaWB5w%3D%3D",
            "zT4OIQ%2B5ioDNUlqWy24SIV1ES164jJYDMu9%2BAbP90vUVVtik%2BF%2FWM%2F%2FVIIw8zqILGzIsTHkYaYmxoha%2F1MgNPw%3D%3D",
            "g%2F1fFYfzRfc42G4Y8dMTkAAfB8wxHZhOhKWHIcrsxhB7xrVRrs1Asp0Z0ZYKWIw9VyW%2FmZ7%2BLvORuFHKDwg6igo%2FhCg11cqrd0CWc7T9Rb4%3D",
            "BbZ8opXR3LxbiSwfdX%2FpxJriOtWBvUsjS95dNl%2FZSYmwq7hWx9ew%2Bci2SEghnsASxu%2FbrNjmZYw%2F1Rjl9oL%2Fu%2FCYAVnD1%2Beg4JDVieEDVdo%3D",
            "htmwwfElC0FZ3BfbV3v5n6i975ITKsjpJWWX8OWb%2FUX9YnfqRgSrkdBKDKwONHzdz84YMQf7RuPWGrIrxSEuag%3D%3D",
            "qu381r7uXdSYDomKaOxBS%2FpdI9XYwY4evcQfa6tLeR7q99Eys7uaY%2B%2FzGm4GYkvXO9FM8Nnn4w5u1X%2BG71dK%2F8NqjOxsNaU0iYfelLHFQ0A%3D",
            "bFyOHpwAFWvLMUL4RhjKvHk5K0b97l9Tvs0sdH5EDZLUpEHh5ZfWfJlg4yscq838EYTiR1nkJpcRX5mEcGo8Hw%3D%3D",
            "Q7qMh%2BvSInaka57hg8vOe%2Fsus3l0SrAfIS8AAR%2FKJHaOLXuhXWOaijYvLG4McyQykTgeBsTy5aeiOsfrLl0rgQ%3D%3D",
            "aWs2PpD2nftjybSHv02XGxMJ5pj20cjDZtepONYziIgwYRmyg%2FVbyF1IMzEWgCmXNCixQlUL9gY4I4gyRQj8zg%3D%3D",
            "faS%2Fgj24%2FhRcy%2B4AMsSLKbvOSDNzwnWr7AOJk%2BEemo9KenXRXnLO4zziR0LBGZa0iQ01Alwhju4%2BvUf35VXKppFww3zXzm%2FtpPSS87l06ek%3D",
            "RqaGM0iS4AzKEA%2BEqbuRaJnLYKgEN0IH1WiL76r9XgQ3dQjfgy3rAY4TzL5eTEi4T%2BJAGGDM2afeTgqwL6AlrjKldvO7oonqyw1q%2FlNS4a8%3D",
            "tZEj9XYAqtIIHkehv5bYiWOl8SBEL0n6nr2ke4Jfa%2B5Mn1uW2IITkdkts%2F6kgQADyv0znT5m4ItzV2u7d2QqAQ%3D%3D",
            "T0vlC9J83MQWfN6AW3DP7Uw6AIF8EgXyXigxRc%2By4t8DwC7N4MkBufLteyePSS5AEZCdZFxgpC22iOaXsNoHZg%3D%3D",
            "gL4ooFfDnrvkikG9fXyRzmrDT95FJPol5c0qKdTSLaUnKv44bWPWLGxsYmSgjnduXXII68yWJpfM7O0e8%2B5qsw%3D%3D",
            "qRIL6%2BueD5624KIajrt0NBKOaXtT4fpBGde8MJssszEM66l5m0WQ9R31X1y%2F2tV2Sf5CJp%2FvoYVh0gcJWJEJTQ%3D%3D",
            "5ryoZrcpGp%2BwAtzRk7yKyBKNm38tM6WgzYGSEozAdnB%2B%2BpYKbxLGz1MhTq3kU3PgVWNpRBjiqV7Lu%2F74DlpWZg%3D%3D",
            "k%2BmYv7g7ezYmEDiAl%2FZ1AQRit7TAiG%2BDC%2FmJXFmMQv%2BhvFf13vjk%2BNm%2FVUtsiBwUMM8q6q8iV4SXtyZKPLqi0A%3D%3D",
            "A3rhpyL0EYWzIEKFVOm3s6XUxOlrIWMW1hg315i1AJ%2FD%2BBETRoCI0RFffFL1kXVnupRgmy%2FAX1oj9GZehHlOvg%3D%3D",
            "RsEvZ5wh4V65feae5gjicnEGFUf0Mh3pydKFs7HyQHowtaHAPa8Ytz2f1i8rl0V5oX4YN0lRyhjnhoDJEJhHRy9xD%2Bq5wzf97qekoglVUFA%3D",
            "sA0g%2FECTtTHmSc6KdzBbljsVLjtKB6YZUFWxZCxqsP%2BrYdkrzwCWWqfV4R1MWp9TZOx%2FzivTX1%2B5NQgTBy8PRA%3D%3D",
            "Dll3x3rit6OcXdhs%2FvSzMr06wPJULlKhIoWyd5OhDmMftEQBmGLsYUwPmEg%2F10X1zFpHTJOhCJ%2FhffyfoABrPg%3D%3D",
            "7dM31fS6YQcfjZtBWywG3zfeI3zdRb%2FJ0Rz7yVAsqYP9ySKITIwBdjBPp79FUHQkS5EyTXglIXNvrzlJaa4IKg%3D%3D",
            "EVylZ1C6Q0o23snID1u4pUbjiQVYjJgSZvxK41RnT4I8e0ik1vVLXIAflIY1CZdch313HutuLJKxR%2F2vJ73T4g%3D%3D",
            "2tZVVyJ1oBcWeSDUnLuGnxbrUdxhBdBmB3ehcZGmvDBastW%2FR45ObcffJHR7TEgjhNH7KgxxddD%2Flj9sHo%2BPsA%3D%3D",
            "WgHkjR%2BLatfl%2FYWhru%2BneQTzXcfc6baKhNFi71vgyPflVEw8PvvStbAN43gjP66YIjRXvmGAJ9GVpdVIvQQ2CW0PrN1ThTQpQBWr8JtqSnI%3D",
            "ic8NMn%2Ft%2FAhaR4c7TtnGPp%2FXjCRssvKed7z%2Fy8UlmPLCZZR7zd73X6Sr1G3jn93t",
            "Re%2BByDdDloe6qokncK40wdQm%2BbYLkVdx%2BKc4FPqD01iyrU%2FaPZcnApmzDa47USfdDcWnFc6wWKf%2BGlyWRYxVCQ%3D%3D",
            "jDZrZOtFr0q6uB9VYX20OZ1TCoFA8Te4oh5iNb9AIdPgaVxQSYjwN3YlPbs%2Bs7VedOS6hFdXwfMwj6%2BBZYrxCg%3D%3D",
            "wD1OG5%2F3ntua8j8EqsFMJcYhIAce01dcJtm8HngkL5EePEwjVQzJ27tVnigZqHW6kOUTLced1%2B9jbGQ7N5cOVg%3D%3D",
            "j11mb1xRCiHyGeHIBcIOtLVOSyibyqBeocLgKWU5dYpGgzgXFDgT%2B%2FuxEp7P%2B1HyYfCPaD%2FIk7Yefc6EhBtaFg%3D%3D",
            "dofWtoAPZfIrP9orMEGhbFE6a8BxgVG5oJdTA72N8KYfGIBn36dSg3eON8vDy1oQbm3mfQ%2FwQhLZM0YwUWR1iA%3D%3D",
            "%2FlO%2FlJrT2xgSvlEw9Ptm%2BAr8Vs780o%2Fm%2BKufZxsyLhPHXKrCqNb0T44N%2FdwzzTKMst0ArtryLYq4tICoMZjPqQ%3D%3D",
            "gfkbPZFZN4cO0VLetS9Ia2g5xrZEpNoLt7Df8dCWD%2FH4hVcjKxiUfP8rWo3CQJ9Dp%2FWiQkwX%2Fi5C5AUkg6ErcA%3D%3D",
            "Y5HEZi7gngP8qqMH6jba9K71uNz8gWRI5j%2BmHC2rbDz%2BMV1uSR9ug4ok213AdLVsmBwZXRcQDjr9Ir5HHQPFxA%3D%3D",
            "bExppomxan7%2FsNvktslhYkoYjBjpYE0g9L%2BqHWpe63t7XiAMnCA2a7rtRBwxmmks",
            "FlvASmM43l3QAOPPducRamJbyKPBMYyJFIV5A3F4IdI%2FW0rBIrxYb5GG7hLGq6YUbRpRVU09EV5w9YCcgdm38A%3D%3D",
            "d2wujgSVOQTk%2B9axpPAU88Mhai38LtJQHyDhPiHYhBx2kD1rgEGZaVCYT0YBDcQpJP%2FsuCxjgNlsNmEwQZXrDw%3D%3D",
            "58sEs6qKYKxP%2FEU7%2BTftm1OXXHCeEUkDhZ4ZZt58elfMoXoRQM7cexUgqqa9ULUW1ZG5LBvOFTYj8yhqGZSCZA%3D%3D",
            "YW7MDi7bKrqFPHeFSjyshYdmSnUQT%2F%2BnOUO%2Bm02NaSgZkZfaM%2B2l0RipkAhVFErlmC3mnJVJOYAmSIwVDHGefw%3D%3D",
            "4F6vPa%2FMQP%2FF2%2BiPM%2B5Oo70ZSPiXCQTsO6sgfFlsCxuCQwovvrGFzQxQpQ%2FWOb%2FcmEcl9Gfq8OYi1yvQT%2BF3iA%3D%3D",
            "btH5ICJFv3B4UZUNouEFMGXOYSML4APtaMRKgwx99SoucdVIc2kUjxPrnSMM4P2OB6jDII7To3h0mlrSHUlwQw%3D%3D",
            "zq6lZinJELBWf5Me3cVqr3rzX2rWCmjBpRQFvjHjTrKYMp9iJd8XHCC8xwok0rv0eTppso7me%2Bb5k7uT2EAZmg%3D%3D",
            "SnZqKt58%2FIUBSKB6ulZXnQeZ9rSEmi16cy77sEncuUd3EUUG9yA9koPktt4UF04VngqiZws0XYQHRSX6DEnMEbT05tFytw2laIaMCY87Arc%3D",
            "5f%2FlfYplZMQplR5JAfZ2z1PiosfW3L8n%2BMHYlUycfESF2jDYvKjaQhPrS3OK5oX3GOU5We1%2F32AcuQSV0JCG424tRqHwdcnr7HCjWL5Q8MY%3D",
            "Wd95z5LApqGqDc7iGfIhLUmx4LoblDgQoHl4wIKSA4ZR14p7cO1fcyS1sPgz7VFi7Tk3yjGWVgJ5zr%2F0DIfeYw%3D%3D",
            "tbOqW27YTNaMPppyrJmrAL4EgD9agQtT1RGPetFX67mJ9H0nmANhXMlvjQ7Gd8tzLEIAxNuhE0wctqHI9oZ%2FqQ%3D%3D",
            "lntdKS%2BinwspcP8MivCJx2s0p6bRMeC%2B2lAoEf5qBVYaCS80mEVnap3J%2FJ1WHsQCacAdI7nMXAlbN7f1%2FcpjYQ%3D%3D",
            "OpFF8hosPKfgL0%2FvnNravPQ1qkhEZ%2FdrBZ0ExKn2zm1t2GJ8XdEvLyBm6RA0fXPa5%2FlVd%2Bx4QWYqjZ6wnx%2FOUA%3D%3D",
            "e5%2FjRuyfKhSidRiC%2F4cXcCvkb9RTLsZp6RWqNb1%2Bsk4Nzf03cyBtaTzjtTbifW%2Bo5Qbf2JNNA5dFa62Y8jGttw%3D%3D",
            "jGenhEOuZJRXBcOrunmnZV%2Fir8SJjjEnaOqKR1u%2BW6Zp%2Bc969EJwmpFH9%2B2JrkV%2BguWEG%2F8A4phj7cJz95Scfg%3D%3D",
            "%2BtVF029FThcDIPV1z2pivHxgJLz3f%2BrcyC5%2BdiqFMRH7xqj3tDxwrKUi2Hq9sLpsAQL83fabyUz4Gvkj1hPJZw%3D%3D",
            "sBSDbYdr6n943MojNZ4%2B1WfUtZEqT%2Fm7PycepaoTz%2BAGnM%2FM%2FrFYuMo%2BtOj0UFQripUp%2Fm1W8T0O%2FpOqxBeYew4KvPN1SbcBZ2tExgWF9ds%3D",
            "W7NyLTTTr%2BNeJ%2BuXK13lTi2L%2Fcy2KEaSsf6lxB%2BNK9m28xH7FD1Ozze%2FgaSgYObdMdj3vxR6VTHjWDzeJlex%2Bw%3D%3D",
            "QHHWqvGPmeKS1870%2BpyQTT09I1f237n9Omi%2BfATSf1OaU4QqnjhgJEKxDKZuWA6PaD%2BdqWIUf2%2BCjPnEvF7rOw%3D%3D",
            "N2rI94Xi%2B4Vtv43biEDYSQh3%2BldKvPirAhbij8Fxt7dMI1dupFu%2BAjgCFh7PEMh2JEKJYEaskaQrCipAwtY6FQ%3D%3D",
            "gJtsjrS%2Ff%2BNZ8mg6XDXVjFM7hEufbeGZaDO4BhIMJrJ0SpFIKkfVm0bKIlPGYSyip%2F9mfyjzciBWnEQeRZXzGeQLdjfPV1ETHCaUyGBzeFw%3D",
            "jZr3jFoyunqBE4sKT1v2i69Q%2FKcwVIDPWS%2F3qYPRkkKB2ihzDyzBhWn%2B2PzlKDvt42UKb%2BHx1aju7JjdM%2BWO1JYHSsKo%2BTdY9Aqwy69GWN4%3D",
            "rueLGib13Hof7rtqVeJHNStx16Cfrw7JrrN%2FjCdkE5GUgXVHTkcF9BHQ%2FEydqGSno%2FT1T9ssMXI1dULeRy3akw%3D%3D",
            "gqhsaH0N5scLjr45C0FyFQe0WDD3%2BpgD2j6bsHxMYIv3fb8OGn6twCOiizztH3JqVcMpdRRlFnhTFuAF%2Biffd9JdsWAV3zHcpB3m%2F%2FedW0Q%3D",
            "CoX6c2TEzub2tRdQxEWyMjq8w0ua3CsIQwHjUrfMbM8JX22oorKVTTjkiONCbt8vCQa7uVrkJGhqI8pPOx8Aeay5dLFdb8WPjjiOkwm%2F9Hk%3D",
            "GBnBjn21z2f13nZvWvMkBS9NElgNhZgk1maFRIQg%2BYrv8HBMMYiAKv1BDDyGND1hrC9kGHvpGQjHRuQii8IfVQ%3D%3D",
            "m%2FvsyoNAa2acu5Od2vIUO3qpfWaiu4hUQ6krsqCchvIq7l8OL2DabF6xWjcySsOTaW1s%2BHDLYaDu3xXMCa5BiQ%3D%3D",
            "FVnAB3WIK571G64azXB55YuR9Gz55AFOmHIa%2BNIDaXJPnD77JngRV2Bp%2FuOBf%2FQ1VEq6y7k9Fm3GX5cwku9yzw%3D%3D",
            "SSnYWM8XxJH%2BKIIC0V9U3Hwq1AYzzmEFO6K48PDRakIjoPgnXI6Oqx8ANVsrZQc52N5Ag7n5iiF4IjNcKpj55A%3D%3D",
            "82dQswJ3Vd9Cn1NAXtTcWsIdJx75ZcnmWOboevi2rlONHAoCAbILdK498VmK9bQBvTTAr6%2BjHHafENUhJi0Njg%3D%3D",
            "5GhlnfwD5MyPJ%2B%2BB6q0hshyh99Pn0P9FGdYBR6OT5cMQCz%2F8Qwzbo159GoSvQ%2FkRqsN62Y6T4xt0qtZREtwKdw%3D%3D",
            "RjWwaJgTnXWm6HiUsA4OwGi6AYy6oZ26zKvH2wk8%2FogAyg9WQGgf%2Fo7NjxjfYU71U4F6Y5GaGi3bwQY2fw8q9g%3D%3D",
            "RlP88xOomofb3A%2BvW1AXwixsFqLFCrap%2B%2Blld%2FVEJyKOksvdp7QDmFk23HQ9N68P3afb4Z9LOKJjR3Hap3lCLw%3D%3D",
            "iUCRJ%2B1bMB5ZYveSK34wpjAs0vpzMZ7cwHXMb6PwXMlVDjyAy8M%2FOyEpr659iKpBo%2BMY74vQ6k1w%2BPAgAxVcuA%3D%3D",
            "VcURq4V32F%2FVZyoFl%2FUp0f%2Bfl6SHUho8u2nMe%2FYpsJQkwdTvHEuGUy1Ntor9AyVYBg7A7rnrcfwCkVVZMCOjTg%3D%3D",
            "HoYga5DI1uKdf8w7inYClvnOyqnX5MPTr8XFIaHjzBS9jB8lPVrWJswvd07ghz3Jj4XnfCe8oRY0yftUzV0AAiEMofH7nsK5a8Ne1luq7uc%3D",
            "aIq4P4mqS6ZcJePQwR6tQQ%2FRfRPuzHC3OTuOxAG6LkMbPTA3yBfX0XCc4S4gcLXLaaa985HVuXispx6wDN%2BIj3ay%2BEwfb3aqR8yECgBEC6g%3D",
            "WSdNtJwdIdx6v1kwZTyPjr24EgSwY9LZXkEYowmhnLPmSsbW2eXllwmsNiDZogc4xEIESmRSKFHS7LvHKJUuVGN0nvMOtaIw03UdBzidZss%3D",
            "eKa2P2SXtl%2FiLqHLGHTa%2FZfgXO8T%2BiqQKvQmibrcl%2BuqaCPBKmLiFfUGXYlSl5NemRyOtCl62OCeZjKn0%2BBNnYWzd%2FshvVIwB87Z6BsqDlM%3D",
            "CAmLBVPmSW%2BNh6XBTAuurDdLWUmSJpI3coENLXhZUiMEObIp71Vw92WJCmHfiunvsRA2l%2BkaE9Z4d13voE4few%3D%3D",
            "Y4cNlFaXwn%2FYSeagTTWBbbY9%2FarAtivK2axDwhmvSkI72jMvF7o7deeFm2IOSl6bYo0i7nYcYdVCB0j9R3leiBtC2Po8cJpXy3oQN2axcGM%3D",
            "p2MHDYLfaktB3ZhOo7jhx9QS6K5j84TDG%2B9Net2Zin20W7EOW0g0rRaZLv1DHMtngAq2AP7xMhhdbHx9xt3kzw%3D%3D",
            "ZWr5BpHxLWeJUwIxCZv7fPd6641ovdgUTdBjFoPTKSAmXGqM9uf4zSSJsfS0ThMjuVoXbQNs2EgdJew3kdFkhXIg2NlUXseUgUTZ0Qb8Ui8%3D",
            "YAa5sHF18XyNRx0NT3yVP1djpXGBIVrIS9BBIlWaSihoDTQ4VOz%2Fe2IeCvw5KrpPU4oE1idylw4aJOItf6%2F7Ew%3D%3D",
            "L4Mvv%2B0QfiRUpM6RyFUEmo9%2B%2FblX%2FJzcHNy3uuXWP30H%2FEY6grasncKrFwC%2FHh6bIDMcppgcXLI1gwFdJO2y5irebB9sUmoFYSJXGbW4OUo%3D",
            "iNldOLL4fgTkVvrrDd4SFttl%2BSlJT9lqO1VHvKk144RFJpQq7xgDb%2FA3eE7Dzui0TVV3uDFQtMSWFSOnlhJhk440aEoukJZiNb8fm%2BsCLKQ%3D",
            "PS%2F3W5XIGqHhl6W%2FOE2AjHxvqoRy0H26R2dfWINFpFmgexF7J7lArnWNegqc%2BEfhy%2Fx1gIuyzbBrtxejbfGwCHOzfiy3yr3C79A2XLKbVcY%3D",
            "Z8cxlSrAeAnCoMkQganPE9bTqYoqUmu4D51MZ4WK3yln%2Bc6k18A1VEORwl0Ss0yCIyMIX2hej6I%2BCd9eIlJ1UTQmhENaC3pa%2B4g64WVDO%2BY%3D",
            "LxuxSG2kMXRaMfSy58rdJogvGasB5tb5qcI0t6Sby9qHJPuvMgS2JddbnOQ4YYKXCeCLu8jdWpInTjQHD4uHOw%3D%3D",
            "lp8H7yLuKMnfvsPN%2BCO%2FpZwXVPm7zifkWx5wY1yXisbFPKoV4%2BRkBqFYY1j9F%2F61gfMpRhUxUmvUfPDzFuFqQg%3D%3D",
            "CCVEvt2ES%2FnKyqSvpHJD8ADYiRO9NeR6M2KtSM7%2FISsJWZUUGTEQB%2F72XNEu1mEcKITSf0fEtQxowflqshDj3w%3D%3D",
            "7o0plXL9NxTISB0jJI6OiI0nJeKsU6cGmLGdCEa4N16YdRUhs3HcnESnjlbhQQq1L4NoO1ouhcCWV3j8x3ne5AZ%2BdO3S6cPrNnNVDHHYEEU%3D",
            "J1%2FLJwhjsPE9WmFXxdHQgJ62r3zxSyH%2B0WJrrV2ZShiH%2BTcXERvsu3dBb5BIXxVxb95ohBpQQVuA2W8kWwo3Tg%3D%3D",
            "v0ylThH%2BJcnJhz8lo7jowJctmGk%2BfkTpYSzrLbXs4o%2FqzDQ877C6MUPgC%2BBY2H73%2BZ1aKUXJhcgFtgr%2FyWxwYA%3D%3D",
            "K%2B1SFuq6F%2FaZM4dIGQzKvPxrRhgVs1837qPuwBi4nulmEbySOxBl58dGqWtn33%2F5oKywoEcNGLZako3yjsfQgg%3D%3D",
            "HVc259CF1KeGgmw%2FB32Ws6yzdl%2FXhd2QyDNO9exddB9Wi%2FgVl2azuYdXBT4AuKJS8iask2dIf8g2jjtR860g8A%3D%3D",
            "E0D5zUq93ib%2BQG0oofUgbv%2BSw6lR8yPFaSA3Z3RDpouhovkWs6pa64E3rcafKKF6WtT6IYSSxTW5hYsMlCjSyA%3D%3D",
            "6cX9PBbt5oggBZNc%2FUyip97kgzW6eyAw%2Fmy9dvFUxKsf5BJh5rd3A9EmgotJ0EkFvigw3613J0E%2BIw2Jk0QiKQ%3D%3D",
            "IgLs4le6kH1%2F3EUQXonVaPT4QpUoYLvpiHQSmb0c%2Flu20uF2UFBFE0NMBO51fzsqaMnn5rmQLtgphgWmKkT5OQ%3D%3D",
            "xe79kPaLT5bERs8Ot%2FTdBfjd9p5mN5W7SyfqHzV94qz33qpIdkV2wPRt7bfrABroYG33XsG2YffslzmU97bm8w%3D%3D",
            "gF8r%2FaZmXlwMVByoiPO%2BEGNuTPWQebN5F1yMzqGz0vfZniFn4kLXsV1K1COlrfINOvVVb8fq5QW4oe70iG2q2Q%3D%3D",
            "i8G2YDIwFKS3t6XcYyGo%2BGprxzv07GrEJrOfGin0te82%2BPO1EecphkibA4Xk8k9cmLGb9kTxjLuKDZJ%2B%2FQ6g8Q%3D%3D",
            "nQpe9HVk%2Fm0rM9NBMm%2Fbd7OKPWytgyZKNW2f4bd3DNRr8OE%2BSjNtbjLs9mm4LdNsOcGVF%2BD296xNSOktpAuwkA%3D%3D",
            "eeZ8j4SfqlRaI4LpJxTCrroc1eEB%2Bew5FV23uE2nkLarcN83BBcyyF2KQ3JKp8zd",
            "IRRLYykjXciIgOa1vm8jqaLno%2FOmd6FwJj%2F5WQzZnBMiqchuq1pcIYUbWXctNHJfUcXgLYayGmM%2BJrn41u%2BeeA%3D%3D",
            "UAPR%2BTniYwTE6w3dlFace4Z3%2B2XoVqTA3DLX6PToDDMv%2FDoCE5Hqd%2BIoBXF6Vw9hQSuMg4U9zUQUw%2BKnQyyopEfQzAApbVFAMJJUqi%2B0gQw%3D",
            "PS52GjSYVlxD2uIkWI8P7inYalQcT6JqbYQgX3Y9gNvZdbvNdIWKhk9rdsCScHUbtSqhv3h%2FhF1M1hZrAGVPlw%3D%3D",
            "u6ob69DMND1PJzlKirz61%2BPsm%2BZyL4p%2BSM9P63WJCEba2anUsC1XfftHnJAKuEAHXec%2BfKKJ2QHSCeXY%2FRt8pA%3D%3D",
            "%2B2hrdHnASAty2uBhWAOfHs6GNZU%2B4VqjINLfbJCQ12n87AQlpXs7cnDyJddWOE620CA5Ok2BMBbLs0%2ByMCIvJw%3D%3D",
            "zvl8xKaVGC%2BEKvdDGKlcsrEy0qSD%2B24UD10LiBITcLFCA1eyPxz9l30c4CEL%2BgzPz8qNAJ83rN0beollU6Nf4A%3D%3D",
            "zvl8xKaVGC%2BEKvdDGKlcsrEy0qSD%2B24UD10LiBITcLFCA1eyPxz9l30c4CEL%2BgzPBIN6eiY%2Bn6qem9aXc4wOqw%3D%3D",
            "zvl8xKaVGC%2BEKvdDGKlcsrEy0qSD%2B24UD10LiBITcLFCA1eyPxz9l30c4CEL%2BgzPQMTUh5N%2FwKASy%2BrMAxlPdQ%3D%3D",
            "zvl8xKaVGC%2BEKvdDGKlcsrEy0qSD%2B24UD10LiBITcLFCA1eyPxz9l30c4CEL%2BgzPL7IIJNMHQec9ZdW%2BJGqQPA%3D%3D",
            "zvl8xKaVGC%2BEKvdDGKlcsrEy0qSD%2B24UD10LiBITcLFCA1eyPxz9l30c4CEL%2BgzPIXpGoDrWCGYmUfVhK3e4SQ%3D%3D",
            "zvl8xKaVGC%2BEKvdDGKlcsrEy0qSD%2B24UD10LiBITcLFCA1eyPxz9l30c4CEL%2BgzPysiKRKYlrHM4q7LgLIohXA%3D%3D",
            "2ywSv5KyMJyBWWkrXcQL2hrNKC85U5oVCurtIC0ImhWhntiK7liGZ9aBBD%2Fjfghg90a7ryIjmaRZqOnOxjmAPw%3D%3D",
            "JxVjODDMRHR49v5SbKa4i1DYLZHreit9Yta%2BCy17rIqsspdOp5k9UV9JIn%2F91KpyDRF%2Fy8EOhh5%2F9cKwnCVs1m4VVc79KImaknMwXdiVnEY%3D",
            "TeAdqYL9oaoY9%2FPI8O5Hpk5y1jN9B1ZUaLp%2BHw7WybzDuFL9oZ9D4uqRpaKhYfW1cjU%2Bpkc%2FpRFDUVzySK3Evw%3D%3D",
            "nqqMAv%2Fx5KsU21HGrh9RMMuKlnRFdvgxsrXLORLAnzo072s2fCuXGYQ5aqUN0ofGhbsxq01rmmuUvapg1mVhvw%3D%3D",
            "tjvfEDZXJa%2BtC4BvkLzIbXqCmZwnN9KwuVCL%2BftSWpaDVbW%2FK5AF6XW20%2Bhgyy6f%2FgPz4VRZgm%2Ft%2FrvU%2BpDyFw%3D%3D",
            "FECSel7HzGxbaNCxIxDamkhV8%2Fu4oZ988YUkfqW25jNOelCwHWAvZnzfxdefW1wZwp02vAWjmvV46YPjAfenew%3D%3D",
            "ZMAaMZafCLserOTJQoFGKcOpkLAjSbGSjEXAW5w1wvPKmNVmYhne93nuSk%2BKr9DK3EgXFJtjmkodd7yeOFsMu2x5h9%2BZthrj13sLEpuTYrY%3D",
            "7uZupqSfP8BeTgmeAai8Tpz8SmGu6m8s70QHdsg%2BtdfOtebmxU9ovk7zvEKqroSfkus6J3EWnsMd1RiXU9P%2FcEyCBE9HBO6TvZy33yt4LQI%3D",
            "abqVcwWD%2FEp1V28pFXbd2%2Bssm0Tu22am6n81vg8Sm7obq12Plkx9OoyvbtHdbqoS%2FHtM8fERHPuaWTeB85tNeA%3D%3D",
            "pSJ2crdtYW%2FD7i5Wr%2FYeqR4YxhxcFb4VXdDnks3Guk941kwQvKB4Rkwp1kRfedydZZqpOGKaxZQ%2FQ5%2F%2B0y3Mlg%3D%3D",
            "oyWEPbugRMeNn5Wy3Qh8i%2BTpbHR2gpFq%2BXWNE0dv2p7Zhxnu%2FT3krLkEu0lKPNyJXdy0qGO2YhZtD5qCdkP3UA%3D%3D",
            "MiAnea1oRRZODryfBXxUVafR5xksZ%2FbfeNy87jZRdNWE15HZjIpncOaUBGDE4Bzu%2Fgym0rqKoWgm5fxZ4XLndg%3D%3D",
            "YcGKTEXh3KDgzsGZFr3gZ2RJ%2FI7NKDu%2BPkgUxQK356MHsH2%2BVx8ux6xRtchu6RF3pARRvTTJBAJTKlnaq%2B%2BcWQ%3D%3D",
            "RQCGdcVQzaF8vL%2FnIZXs%2F3IUVunygDlahoEVpA6L6UBwVAY6%2FvQQlXjQh7OPx0e%2Ff55bIgg61DKr8tKfa61shA%3D%3D",
            "q1TyIYCdkpDNL%2FcV8xJUt%2FkcKwSAFam8y6YAg56bKVE%2BI5nJ7RvB%2FWPqJCO0eghl0i3blgwQDZhGlW8XR7CckGzJiTKgvKf8DUBXn2jYJhs%3D",
            "2JaZiV8QgGsTQ%2FrJB1KuakAedyaznpAyjxfMgGBMvJc6ly2g4tG80lsXFd2kTxuxawtR6V1YI5R3uQJ1GgLzY1UanSPyYIM%2FYBP5HhLFoDc%3D",
            "b3Jpm6n6JJ2h0UxMfpTchbS7AcpWfzUZg3AlLSkdOubz6%2F%2B6CVEZhcfWkkZ%2BkKZw3j9c%2FH3%2B39SLrJdgo3PHLg%3D%3D",
            "qAe2EnF6Eyz0V05cGCAE38sG1Dmoi4Z%2Blf1WMAeGEV2pH8BkUiaUoOe4vnQCTSgAum2u37LchSMALFb9vk4Scg%3D%3D",
            "QWBJO3QZSXF3M4B091W3gE9j1rKcf0H7IEKvmoltz30IW3fs6oqhZqf8NYl4GtrVvYOf%2Fg%2B%2FDZaDocGW1pldeA%3D%3D",
            "3wkfjoxOaMeKznaQxY5tbKww8ZzqzAddS%2BvvVZjEfMvueT%2BYmlftdpNI3DmKP0k9tuT7bYZm73yj536w3KtQDzUnxDiC83jF120Nej6HC0g%3D",
            "bkaITrl51Tuky8kSseJgkht5oQn4ZrPeJNRa3VUlBMJTqUQkyDzHr5%2BPytf3hifT20E6zJytdUjkwMI%2Fq%2BRlog%3D%3D",
            "PSIOSbBn8VJ9a4DLhkId5xN%2B%2B5WQYgRGToLt96yiGY4oCIUAoJFXn7xL82hN5P10K%2FSl9rEE3B4GNaA9wsOlfTlK4BlC%2FCBXSJ%2BgZKypZk8%3D",
            "%2BFUwLVxMtNkStnOm%2BqWwSa6lmOJGLakD6vW4Trj58ExoOkL2CvrwWd0wp6%2BAxHdN5KdHMd4zhshRZ0GayHO0UA%3D%3D",
            "llZ7GpdyI7TX4L9U4tQBpsx09LNoW8x5NBrvOvtPZhZUnYbqzrkbVF8RuUfWouU3pJRcP7DVifsINc%2Bz7X2FeQ%3D%3D",
            "Ik2X91y83%2FJEIk6VNgFqtyXAyRpX8zXSbH5Z%2F6hksJbtEOMOoQHZySBBL5VDnftfp5G3HnQiQLgGyZJeE0wwMg%3D%3D",
            "8oLpUfoc3homQeH7wqgclxpU9IM6a5tIN37zOx3yP8esmaPgmWCs54bagNAlS3UYmv8SsWAie1FtGfFurtNk2CNd8obgc5Gw4iytClZSBjI%3D",
            "IHxLarOvrW38XsjPMdHK3zS3apf99nxaftVXK9%2FwSvDA43%2Bpi9WKYkfap4AkiiPQkUls%2FzrHNGkBSQSgIbQMLZAxMbtLYHmBOtIWsnc6eis%3D",
            "%2FqwfSkynZzTZWTOb0klIuxuk%2Bw7PZSxIyR6d9Oxfy45cAjxzAz9DLN0gkyK9Dv3H97cST4DMnZ10Db4WBDnTCooKc5HpE3lrbBI4jAWqft8%3D",
            "%2FqwfSkynZzTZWTOb0klIuxuk%2Bw7PZSxIyR6d9Oxfy45cAjxzAz9DLN0gkyK9Dv3H%2BN54koND79om8%2F1jVncPFO5hUDYFDfBFF9VIPb7Y82g%3D",
            "LKYt6uvy4R3DPdXLu6IIx64KIEwhAmuf72gMUb%2FG95JHQj3Nc9USmNSvzO5Kfvkuo1YuanPzbZpkzjuQVzEFCA%3D%3D",
            "RBWyn4ZLy05kc8psmWXzWHU1ajbutcm9AOUOWUmyVhP%2B2xU9U5tM%2BnJWULNKs7RfnnJApOInYKHTB4%2BXxW%2Fd4w%3D%3D",
            "bfHLnpYK0vftSSS8liaedY0%2Bk7rqGLv4aya5yzMBWlstOrLSF5R0pXiyisJvGlHlkIztJua8O3Gwm%2Bz19iU6qg%3D%3D",
            "Qs5FqJeubYFYlybSPsugqBNbm7L50EGQQTWLCqE%2F7mCEm29dXJ7fT8AexWnDtst2wNbrU5NWkalFMbJh0KeU%2BA%3D%3D",
            "pjXx4MwKBRcNcfRdy%2Bdfpd5gdiPF3EgE1Dv%2F4Wy4lHn3Ue8zlbM6efEGZgj8r2XPavK7U63cXkFeaCOJg0ealg%3D%3D",
            "M9a6CeXBs7I%2BmeY1TL40mpBWUl7p%2BKLzxq5vo2Su9HELPqEdCySFa8VGm3Nm9vXkRI6sNxfoocrM1QsT0E5D3Q%3D%3D",
            "3CsLZAshjAFTXAyAjeJ%2B2LOdnP9PvbXw8la7Va1gXAk1SpB%2BvuDJgwmTqinU0Hk8YYHOgiHlgfs33JqW%2B%2FcwEhJB3oZ2sQ446BiqpvbQVxE%3D",
            "AOznfh0gjnxenxDcWDHpQX2%2FSOwYrXz%2BjO8QIEB6Jv2Hx44u%2BRqE2i7RlgJNqLkWeTrIjkyUA8Mf45uIXL5kRB%2FZtdc8XuDK2GyFLX4m6xQ%3D",
            "Eu6EeIwJEqALknEGD59Cho89gNGrRNhj9IBSmd1VuE9jRQKnUMVlMGOD7D4djM7R4r3zwprQOiOrCVN9qZxlmA%3D%3D",
            "SQPazXt0YOPpVHXuVbJkZcd9IIHkE8IppvkHZNm9mlhp0QO2W9yoLgReo9GnP3%2F85TfWGUm36upvh%2Bu04QA3HQ%3D%3D",
            "SQPazXt0YOPpVHXuVbJkZcd9IIHkE8IppvkHZNm9mlhnodBu7tbb4fJAggHdRGm%2FmHVXhZ%2FG4%2BdJ6xwNrrYnVA%3D%3D",
            "lgXuPhaWm7lEWUO%2FixawDwpBH32etQyeSbnd%2Bpgzj3%2FIyCigSEBuzSM0iVdq6JWvKtM1qinqEu%2F2bKVbixnO%2BA%3D%3D",
            "7lquQNP9P%2B321kTE5UQDNF4A%2B1KSHjzrhsAb1SH%2BSijReSZGZHLiQ3e2EIKuuUSThseTrERTPNGM26MXEHR%2Fuw%3D%3D",
            "lRVkRs3ABtbHeaQmRvd4kG0o3ef%2BCI84Na0dQChS5B7Qpb9ns7fBI5CcnWMtkpLTBibDWQtXRDOkd8hCyQp88A%3D%3D",
            "R1pTUay2Wyf0Wzh5QrQ2Xcv3f8bq2DFhK0zZbZfPXtW%2B6TWYdy4I%2B8DvLsrev4kkYZyAweM7VXoVjT6mCqkxJQ%3D%3D",
            "2I0%2FBJZKL06RD2tfXzefo8kHpxyBM3nR2IuHfHPDhQ84C3VU5nPG12X%2F1Jv3VVA%2FFZG1ADHYVjWq%2FNCDyYkEcg%3D%3D",
            "h68WFxXtG%2BnZsRKvCmHYFDqrx%2FKbqmKsDixJ8KP7%2F4eH3QvBewTC7flSwJPt4t8E9Ty8LygPpZRccIFga82yrQ%3D%3D",
            "l1XiuLf8sz7cbMYQEnOAn1lR7eV%2F0YvxMbyHcqR0UXEsCKsPuFXugOxRiSJKHAmkrD0xTd93qk4UzlALBvw8vQ%3D%3D",
            "YcWPaa8ceBB7rmLnGMJXQYHx5EqgEJr3fpFIPPGhMylrsIV3wz2UqPrg2ItThD1rW20KXjS6VQjJJj0zyyxPjA%3D%3D",
            "IS2AFY9zgeBZU5YNvO4n0LIUZCG8ui4VoG83o0hC9zRpprbTDjlILkEjtSHyCpk9hDXlFNZzBuzHzLetgMH9pRWFICGkn4bySIngSL4izwc%3D",
            "rdRq3n6sJIA3MScxuD7o%2BYjza4yeb7j%2F1I28BGAshw0z7Qi234pwcYJX0ODPE%2BZZWjRWhkXk4vqqPN0DmozUqZBIB98XLi5L3%2BMSbPUW8Ks%3D",
            "61RLAuszMU2fQuA6IHjhMnyumH6PSQ3PgxPLD7GFo8WCV6l9GKa2sClRYg%2BJ%2Fsd5DfHRTnZFqhop3rhoy9XrNax0Myrukx0Rrew%2B%2FYgmanE%3D",
            "zgCZuxdINW%2F8RJ236CaXwqjkixF6l217MfRi9bIXUl%2FbfgWxJlYUHhY2dF1MPE7gMuPUMoByzbxb4rh9JR2tIg%3D%3D",
            "lS0N41AgT6Sbtq993ERvi1XSCwBpa0PCYjk2Dhxx3nzD55VIllZs2sgkY5fhnXTLb3LFaCn1WFSI4XQ%2BnSndMw%3D%3D",
            "RR0TwsA3DABBd3s10W%2BXYQOUUqOpWEQId8OP%2BMdnwtxMKw%2B7ai6i9e%2BKs08HMp5KW7aJPPwRZ8Xn5Bl%2F%2F4wvVA%3D%3D",
            "QyVMC%2F1pE%2FqNDaCgqGB%2FiW3rsfVoHjiEGxmoJy6O%2FRe5OimjxMzP4utbu%2FSagsVKBnq59EG%2BBazJUammaNNQjg%3D%3D",
            "QyVMC%2F1pE%2FqNDaCgqGB%2FiW3rsfVoHjiEGxmoJy6O%2FRdiIG8kjQaKaRiRnj4BYmcYMKcCyilHIvtDpbwe9sOFbQ%3D%3D",
            "2YkBrX4VFTgYot16x%2B3aTxyjoOk2CkpDpoii3Z%2FmnhIKbnIvRa34BheZgT5UGDrc6%2FAzp%2BQWSatjt%2BcE0D3v0g%3D%3D",
            "kQ7pX5piMh0QlSn4ljvxvCfZWZostoReuUy0%2FJN6iApo5KuREeXvdacU6XCQkndOWqjT0ELcg21JFvqe2vJT%2Fw%3D%3D",
            "ktPsKYNSt69fTc1YGCPJPF2gJf2%2F%2Bp8UN%2BYjAe8AlDW6eWMfwAsW8R7w8pBaIazsQt64aY%2BFotwV6qJSn%2FEzEQ%3D%3D",
            "4hS07yfvVH%2Bwxgto300mHZcRmIBnzRwQZGcStXNBpS0L%2B3n00tFhjG15ILyMMiCwjmDBQKPMil%2FnwCypbbFiSw%3D%3D",
            "4hS07yfvVH%2Bwxgto300mHZcRmIBnzRwQZGcStXNBpS0L%2B3n00tFhjG15ILyMMiCwbmGlmr25DJi%2F0PFZJCpTxw%3D%3D",
            "rsVkyappTWNeTq1ooLfI93Zf8uIVB90wnJFL6gZykuzmNsUye8lmQs8mKEB%2FZeqM8jcmAkNXkqDISCxIINxPnA%3D%3D",
            "iaLgBrMOc1R8fLG9Z1bf%2BUe%2BCu5vtBiDVvjoeM%2Bq1Zmm9x0De4WTsgi5YQb%2BIibNtjfjTAmKOTvtZ5XpQmTHRA%3D%3D",
            "CMoa7%2Fr6fk0OvSg1R33KEIVhVslIF2Q0rzr0FwTbzlOsK75gnbtJes1reGIoffWgHBcfhzGP20GSDaINDEguXg%3D%3D",
            "mbmUX4lqYLiHONgIe%2FvHQz2XCulZ6FH4LPYGQWmg18xllfZJvELgvmRSebfn9Kd7m3ViXlN%2FjrdLQQK7%2FO8R4g%3D%3D",
            "hptZYBgBJRn7o8FR%2Fbn%2FTfwEsXxUd56LfgSpajPhKZzM79R0nIESbUohUEv8rb%2FxAI0Hnlp0zFiazBXXEZt7Dw%3D%3D",
            "yxXTGHbohj9o9G2ApfmXP2O%2B72rEPwJR2rpvvOugoQvc6kKL0q%2FevuZ1ahNlQc6L31Sdm0dJjIlVMXAAXOs3FQ%3D%3D",
            "cWsE0r6ogPBTTgTvyk3o7kQwrwH477bGKjndmH9lIFaPaO6MOyjLJwqX8fR5VNrs4sMh0HSzmyaxYqkpjoZi7SyO8EaRXXaoh3Fodbmd9y0%3D",
            "cWsE0r6ogPBTTgTvyk3o7kQwrwH477bGKjndmH9lIFaPaO6MOyjLJwqX8fR5VNrslYTiqcBCOPkhewKv2vsEgFlUPFZT1UBLM%2Bn0QJGBfqE%3D",
            "zZ%2FVDsAzM65cT%2FKOTUQcBQuP3hZlBadSJMsrKFA74jHztE34JasanQzvwBgwoheAtEiw2VZTagnEGGgLP3oL4g%3D%3D",
            "Ekjp5JCa3bh2uNkD0HadutD%2FZmuhIUkYDLz%2B%2BEJ7BhplCEEOmapbHn3zXV78LVErTN4NSnmoMSPMjoNvrb8Ufw%3D%3D",
            "vJkfAXU8fKQL5hx59EHIkR2ZpdpRLd4LAUusjdUgVZoK7Kpgj43%2FvfDcX0QWZsi4%2BzM04S3ow%2FdBkmkb6CfVUg%3D%3D",
            "H5k8lKH5vgmjK7gCwmP92oUktIUhR1IW0c3BO5BG3YH4ZoQy5HvjmrjOnC4h4JOF19bAA6NRKV9cT43NMUxFLw%3D%3D",
            "DuB6cfjtIWxFmzXv0RYRCT3RD0j2RW4zPugB7nqe7tsQNlISUTTOVEog1FZV0N3bJnKNdWCil7c4bV%2FhrbmlSrrs99%2Fod1ADr95Z0E%2BcLSk%3D",
            "xTiHywtU7Roy4xX7hzIFOU7Xv0RAWfdwFBQZxeEKPqjUGUEsKOYw3skn8m6rZx4cuNgM%2F5doZsnnLZLFe8%2FswQ%3D%3D",
            "T5XPpCIT5norB4dPgZhm92QMYaQPETrfGQRGf%2BlA1uzpzT%2FlQH8oC4m%2Bf4rHyyJ%2FHm24KkzGE2IweSmp7eKUR4sGE5XnJWU8zsIOsico3Sk%3D",
            "xNe1J1%2FfEVOK8Mp56VTNjkhZIkqhsG0zRH5AGsb3ymj5vYZpDeZ9O4wT9m%2FfFfBCFerlPT5PD11M2YRW21XrIw%3D%3D",
            "KfRMpiLz%2Bd4kIRLVb8ZNBBRZvUZyUeh3UXqmGPYOGgRW6V8M6VhdzdkYaRwbmppJaaUwrwCofZhePp1us5bP5Q%3D%3D",
            "OZC%2FniNtCzjn6lZETXxfyuibBXfGdTdqJXF8gGIYHk3kFK5Rnvf%2FrB92Rc1ISF8zytpSgAqE9rBRS%2BgPzZnJxReyP25cdi1LtluxEyK8Kfo%3D",
            "B2lkGQvC6gUVntsiNMx%2Fc4EodeGPC6zT0l0EJ%2BvWx3OQKTlDJMsgKPEuQOlwRgDRTvyQxsJAHC6XS9zaK2EiAg%3D%3D",
            "KcDOxKKHh8zezDT0XPf2WCxF%2BstUjoPAj%2BWiMtMyZOsrNQgm3IU8nMqUHfxpH3tJLFxuJdEpQYrDOvw9Q09VVg%3D%3D",
            "KcDOxKKHh8zezDT0XPf2WCxF%2BstUjoPAj%2BWiMtMyZOsrNQgm3IU8nMqUHfxpH3tJYH%2FyezflH92OgmEk%2FT9INQ%3D%3D",
            "KcDOxKKHh8zezDT0XPf2WCxF%2BstUjoPAj%2BWiMtMyZOsrNQgm3IU8nMqUHfxpH3tJwfBY6D5qEL6IzAyVn2%2F4Xw%3D%3D",
            "7FGbTHxa1%2BxQJwqfG0Hdj9ls8nRPB7KdGyurzqzRBCqbOwIH%2F2ikK9Mhx1lqv%2FEZ6IKZZKmZ0WM11IQqML6HCg%3D%3D",
            "7FGbTHxa1%2BxQJwqfG0Hdj9ls8nRPB7KdGyurzqzRBCrOkaJlwoUn%2B%2BwjMX%2F8AOsBcE%2BB52wZpWqPujC%2FGKwDjw%3D%3D",
            "QUhBize9LDx9eCXEDGKbLD%2Bfn7ZvRd9NedwMJabS0xHHEhMqSu%2BDBCCUU3JwI3Ii9V1tx9yNLbGNQasXxyybRA%3D%3D",
            "IX6wLmqgfzAhyGmtKyK3wnKzZoBFsqaH1dt8Pr4vJhpHEvO1AZJh3LD%2Bo%2BQDlOd503WtYQ4z%2BoLuyXCezCdOnA%3D%3D",
            "IX6wLmqgfzAhyGmtKyK3wnKzZoBFsqaH1dt8Pr4vJhpHEvO1AZJh3LD%2Bo%2BQDlOd5fWbjv1zPETIEFh8pBPweKw%3D%3D",
            "WxgDNvmDgwkmedCm2SPj862cEEorQNcDga7my35R1xCOW6pbpkE2AkWtxx3UxvnVQJoTo2biWqYALUN9q%2BTyew%3D%3D",
            "W1AX39%2BZTq189ciTAV00E0tGemqt0BWldMOPADZG4ytVp1wvE5P0Xn3dUqN0UIyqFJBuLoOKrN%2BRtXdn%2FBoBPw%3D%3D",
            "Udy17vSp39BONUXvyvUKOukrMFtm932kS586wdkAVd%2BgyA8Q6U87JVvzGuGELInOxxp4FDm89zodbNM2%2FP9IZMdrAxjS9yexUPBCVrZMEFU%3D",
            "qlNgtN24M1sok4b%2FUVG9x9R6F6SD5NLRI%2FA9CZbN5z71F9lAfwtTIemegGQbNACm5T9gXCLu%2BZ0ge%2BswKH%2Bw5Q%3D%3D",
            "yrF0NhFuPpPAzXgpcEpmq0HkEg1Xx72CdX9X8BA4yZqHiDWTIECg9ns9j65qJp2thXN%2FN8iggbqIMff9JYA7WQ%3D%3D",
            "KZBvGsEC03TQLonjPVU3PD3cub3d5SUUwGOZoQ3efdRQ0rR7Yi3U6NprKXrt4Y6uRYOL9A%2FWo3BHdZ9uofeaIQ%3D%3D",
            "9d6dtGDcvE%2B%2BSd8SZASx73bQe4El0DAVelIIFI2gKczQ1o7ksbT02PQ14168Q8k0CjpJeDaD3qIlO4a3edDLFQ%3D%3D",
        ).forEach {
            val algorithm = "AES/CBC/PKCS5Padding"
            val key = SecretKeySpec("DroidJamIndonesi".toByteArray(), "AES")
            val iv = IvParameterSpec(ByteArray(16))
            decryptDo(algorithm, it, key, iv)
        }
    }

    fun decryptDo(algorithm: String, cipherText: String, key: SecretKeySpec, iv: IvParameterSpec) {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val encryptedValue = URLDecoder.decode(cipherText, "UTF-8")
        val plainText = cipher.doFinal()
        println(Base64.getDecoder().decode(cipherText))
    }

    fun encryptDo(algorithm: String, inputText: String, key: SecretKeySpec, iv: IvParameterSpec) {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val cipherText = cipher.doFinal(inputText.toByteArray())
        println(URLEncoder.encode(Base64.getEncoder().encodeToString(cipherText), "UTF-8"))
    }


}