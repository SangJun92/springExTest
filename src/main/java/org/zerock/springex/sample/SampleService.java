package org.zerock.springex.sample;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@ToString
@RequiredArgsConstructor
public class SampleService {

//    @Autowired
//    private  SampleDAO sampleDAO;
    @Qualifier("event")
    private final SampleDAO sampleDAO;
}
