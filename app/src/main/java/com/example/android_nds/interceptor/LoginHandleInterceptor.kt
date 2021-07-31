package com.example.android_nds.interceptor

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.android_nds.ReqErrandFragment.Companion.TAG
import okhttp3.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


class LoginHandleInterceptor constructor(val context: Context): Interceptor {




    override fun intercept(chain: Interceptor.Chain): Response {
        // 브라우저의 쿠키를 안드로이드에서 구현하기 위함.
        val preferences: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        // 쿠키 가져오기 - 저장은 로그인 버튼이 클릭되는 LoginActivity에서 해줘야겠지?
        val memId: String? = preferences.getString("mem_email", "")
        val memPw: String? = preferences.getString("mem_pw", "")
        val memNickname: String? = preferences.getString("mem_nickname", "")

        val charset: Charset = Charset.forName(StandardCharsets.UTF_8.name())
        Log.i(TAG, "charset==================>>>>>$charset")
//         쿠키에 담긴 사용자 정보를 body에 세팅하기 - default는 빈문자열(예상되는 결과값: 실패)
        val requestBody: RequestBody = FormBody.Builder(charset)
            .add("mem_email", memId)
            .add("mem_pw", memPw)
//            .add("mem_nickname", memNickname?.let { String(it.toByteArray(), Charsets.UTF_8) })
            .add("mem_nickname", memNickname)
            .build()
        Log.i(TAG, "mem_nickname==================>>>>>${memNickname?.let { String(it.toByteArray(), Charsets.UTF_8)}}")

        // 로그로 출력해보기 ===========
        Log.i(TAG, "memId: $memId, memPw: $memPw, mem_nickname: $memNickname")
//         쿠키에 담긴 사용자 정보를 header에 세팅하기 - default는 빈문자열(예상되는 결과값: 실패)
//        reqBuilder.addHeader("Content-Type", "text/plain; charset=utf-8")
//        reqBuilder.addHeader("mem_email", memId)
//        reqBuilder.addHeader("mem_pw", memPw)
//        reqBuilder.addHeader("mem_nickname", memNickname)

        // 파라미터로 주입되는 Chain에서 Request 객체를 꺼내옴.
        val req: Request = chain.request()
        val reqBuilder: Request.Builder = req.newBuilder().post(requestBody)
//        val printUrl: String = convertNu
        Log.i(TAG, "request.url().encodedPathSegments ===> ${req.url().encodedPathSegments()}")


        // 가로챘던 응답이 가던 길 가도록 보내주기 위해 응답 준비하기
        val res: Response = chain.proceed(reqBuilder.build())
        // 응답 반환하기
        return res
    }

    // MN) url 중 숫자로 된 부분은 와일드카드(*)로 변경해주는 메소드? - WHY?
    // encodedPathSegment: ..../asdfs/wer2/345/sadf...에서 asdfs, wer2, 345, sadf 각각
    private fun convertNumericValue(encodedPathSegments: List<String>): String {
        val stringBuilder = StringBuilder()
        var convertedPathSegment: String = ""
        for (encodedPathSegment in encodedPathSegments) {
            /* [Boundary matchers]
                참고: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
                ^: line의 시작
                $: line의 끝
                [0-9]: tntwk
            */
            convertedPathSegment = encodedPathSegment.replace("^[0-9]+$".toRegex(), "*")
            stringBuilder.append("/$convertedPathSegment")
        }
        return stringBuilder.toString()
    }
}