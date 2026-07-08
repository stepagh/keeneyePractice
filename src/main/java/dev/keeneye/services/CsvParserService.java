package dev.keeneye.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import dev.keeneye.dto.UserCsvDto;
import dev.keeneye.exceptions.CsvParsingException;
import dev.keeneye.exceptions.FileIsEmptyException;
import dev.keeneye.exceptions.InvalidFileFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvParserService {

    public List<UserCsvDto> parseRegistrationCsv(MultipartFile file) {
        String contentType = file.getContentType();
            if (contentType == null || !isCsvContentType(contentType)) {
                throw new InvalidFileFormatException("Csv format required. Got " + contentType);
            }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            throw new InvalidFileFormatException("Csv format required2");
        }

        if (file == null || file.isEmpty()) {
            throw new FileIsEmptyException("File can't be empty");
        }


        try (Reader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            CsvToBean<UserCsvDto> csvToBean = new CsvToBeanBuilder<UserCsvDto>(reader)
                    .withType(UserCsvDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')
                    .build();

            return csvToBean.parse();

        } catch (Exception e) {
            throw new CsvParsingException("Parsing csv file failure: " + e.getMessage());
        }
    }
    private boolean isCsvContentType(String contentType) {
        return contentType.equals("text/csv")
                || contentType.equals("application/csv")
                || contentType.equals("application/vnd.ms-excel");
    }
}