FORMAT: 1A
HOST: http://localhost:8080/

# Tarefa
Durante o nosso desenvolvimento, tivemos a necessidade de fazer uma integração com o ImageMagick. 
Este software permite, entre outras coisas, obter a informação técnica de uma imagem (
ex.: altura, comprimento, entre outras opções), bem como redimensionar imagens.

Desenvolva uma API que, recorrendo a integração com o Image Magick, permita efectuar as seguintes operações:
• Obter a informação técnica de uma imagem (altura, comprimento, entre outras)
• Redimensionar uma imagem para um determinado comprimento / altura

# Descrição

Todas os recursos e ações podem ser testados executando o servidor incluído e acessando o seguinte link [http://localhost:8080/images/form](http://localhost:8080/images/form)

- [Listar todas as imagens](http://localhost:8080/images) /images
- [Exibir as informacoes sobre uma imagem](http://localhost:8080/images/info/img1.jpg) /images/info/{image_filename}
- [Redimensionar uma imagem](http://localhost:8080/images/resize/100/100/img1.jpg) /images/resize/{width}/{height}/{image_filename}


---

# Images API

## images [/images]

### Lista todas as imagens [GET]

+ Response 200 (application/json)

        [
            {"filename":"img1.jpg","info":null},
            {"filename":"img2.jpg","info":null},
            {"filename":"img3.jpg","info":null}
        ]

### Upload de uma nova imagem [POST]

+ Request (multipart/form-data; boundary=-----BOUNDARY)

        -----BOUNDARY
        Content-Disposition: form-data; name="file"; filename="img1.jpg"
        Content-Type: image/jpeg
        
        4AAQSkZJRgABAQEAYABgAAD...
        -----BOUNDARY
        
+ Response 200 (application/json)



## info [/images/info/{image_filename}]

### Exibe as informação sobre uma imagem [GET]

+ Response 200 (application/json)



## resize [/images/resize/{width}/{height}/{image_filename}]

#### Redimensiona uma imagem [GET]

+ Response 200 (application/json)