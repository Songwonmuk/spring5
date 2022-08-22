package net.softsociety.spring5.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 리플 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
	int 	replynum;			//댓글번호
	int 	boardnum;			//본문 글번호
	String 	memberid;			//작성자 ID
	String 	replytext;			//내용
	String 	inputdate;			//작성일
}
