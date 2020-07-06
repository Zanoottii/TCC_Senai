package br.senai.tcc.nursecarework.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public abstract class Validacao {
    public static boolean isCPF(String cpf) {
        if (cpf.equals("000.000.000-00") || cpf.equals("111.111.111-11") ||
                cpf.equals("222.222.222-22") || cpf.equals("333.333.333-33") ||
                cpf.equals("444.444.444-44") || cpf.equals("555.555.555-55") ||
                cpf.equals("666.666.666-66") || cpf.equals("777.777.777-77") ||
                cpf.equals("888.888.888-88") || cpf.equals("999.999.999-99") ||
                cpf.length() != 14)
            return false;

        int d1 = 0;
        int d2 = 0;
        String numerosCpf = cpf.replaceAll("[.-]", "");

        for (int i = 0; i < numerosCpf.length() - 2; i++) {
            int digito = Integer.parseInt(numerosCpf.substring(i, i + 1));
            d1 += (10 - i) * digito;
            d2 += (11 - i) * digito;
        }

        int resto = d1 % 11;
        int digito1 = resto < 2 ? 0 : 11 - resto;
        d2 += 2 * digito1;
        resto = d2 % 11;
        int digito2 = resto < 2 ? 0 : 11 - resto;

        return numerosCpf.substring(numerosCpf.length() - 2).equals("" + digito1 + digito2);
    }

    public static boolean isCNPJ(String cnpj) {
        if (!cnpj.substring(0, 1).equals("")) {
            try {
                cnpj = cnpj.replaceAll("[. /-]", "");
                int soma = 0, dig;
                String cnpj_calc = cnpj.substring(0, 12);

                if (cnpj.length() != 14) {
                    return false;
                }
                char[] chr_cnpj = cnpj.toCharArray();

                for (int i = 0; i < 4; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                        soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(
                        dig);

                soma = 0;
                for (int i = 0; i < 5; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                        soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);
                return cnpj.equals(cnpj_calc);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isData(String data, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("pt", "BR"));
            sdf.setLenient(false);
            sdf.parse(data);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
